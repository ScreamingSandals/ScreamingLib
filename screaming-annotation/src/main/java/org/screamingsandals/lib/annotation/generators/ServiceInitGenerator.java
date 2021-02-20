package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "builder")
public class ServiceInitGenerator {
    private long index = 0;

    private final MethodSpec.Builder methodSpec;
    private final Types types;
    private final Map<TypeMirror, String> instancedServices = new HashMap<>();
    private final Map<String, BiConsumer<StringBuilder, List<Object>>> initArguments = new HashMap<>() {
        {
            put("org.screamingsandals.lib.utils.ControllableImpl", (statement, processedArguments) -> {
                statement.append("this.$N.child()");
                processedArguments.add("pluginControllable");
            });
            put("org.screamingsandals.lib.utils.Controllable", (statement, processedArguments) -> {
                statement.append("this.$N.child()");
                processedArguments.add("pluginControllable");
            });
            put("org.screamingsandals.lib.plugin.PluginContainer", (statement, processedArguments) -> {
                statement.append("this.$N");
                processedArguments.add("pluginContainer");
            });
            put("org.slf4j.Logger", (statement, processedArguments) -> {
                statement.append("$N");
                processedArguments.add("slf4jLogger");
            });
        }
    };

    public ServiceInitGenerator add(String name, BiConsumer<StringBuilder, List<Object>> initArgGen) {
        return add(List.of(name), initArgGen);
    }

    public ServiceInitGenerator add(List<String> names, BiConsumer<StringBuilder, List<Object>> initArgGen) {
        names.forEach(s -> initArguments.put(s, initArgGen));
        return this;
    }

    public void process(ServiceContainer serviceContainer) {
        var typeElement = serviceContainer.getService();
        var initMethod = typeElement.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && "init".equals(element.getSimpleName().toString()))
                .findFirst();

        if (initMethod.isEmpty()) {
            initMethod = typeElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                    .findFirst();
        }

        if (initMethod.isPresent()) {
            var method = (ExecutableElement) initMethod.get();
            var arguments = method.getParameters();
            var processedArguments = new ArrayList<>();
            var statement = new StringBuilder();
            String returnedName = null;
            if (method.getReturnType().equals(typeElement.asType())) {
                statement.append("$T $N = ");
                processedArguments.add(typeElement);
                returnedName = "indexedVariable" + (index++);
                processedArguments.add(returnedName);
            }
            if (method.getKind() == ElementKind.CONSTRUCTOR) {
                statement.append("new $T(");
            } else {
                statement.append("$T.init(");
            }
            processedArguments.add(typeElement);
            var first = new AtomicBoolean(true);
            arguments.forEach(variableElement -> {
                if (!first.get()) {
                    statement.append(",");
                } else {
                    first.set(false);
                }
                Optional<TypeMirror> typeMirror;
                if (initArguments.containsKey(variableElement.asType().toString())) {
                    initArguments.get(variableElement.asType().toString()).accept(statement, processedArguments);
                } else if ((typeMirror = instancedServices.keySet().stream().filter(type -> types.isAssignable(variableElement.asType(), type)).findFirst()).isPresent()) {
                    statement.append("$N");
                    processedArguments.add(instancedServices.get(typeMirror.get()));
                } else {
                    throw new UnsupportedOperationException("Init method of " + typeElement.getQualifiedName() + " has wrong argument!");
                }
            });
            statement.append(")");
            methodSpec.addStatement(statement.toString(), processedArguments.toArray());
            if (returnedName != null) {
                methodSpec.addStatement("$T.$N($N)", ClassName.get("org.screamingsandals.lib.plugin", "ServiceManager"), "putService", returnedName);
                instancedServices.put(typeElement.asType(), returnedName);
            }

            var controllableForMethods = new AtomicReference<String>();

            processMethodAnnotation(OnEnable.class, "enable", typeElement, returnedName, controllableForMethods);
            processMethodAnnotation(OnPostEnable.class, "postEnable", typeElement, returnedName, controllableForMethods);
            processMethodAnnotation(OnPreDisable.class, "preDisable", typeElement, returnedName, controllableForMethods);
            processMethodAnnotation(OnDisable.class, "disable", typeElement, returnedName, controllableForMethods);
        } else {
            throw new UnsupportedOperationException("Can't auto initialize " + typeElement.getQualifiedName() + " without init method");
        }
    }

    private void processMethodAnnotation(Class<? extends Annotation> annotationClass, String controllableMethod, TypeElement typeElement, String returnedName, AtomicReference<String> controllableName) {
        var result1 = typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(annotationClass) != null)
                .collect(Collectors.toList());

        if (result1.size() > 1) {
            throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @" + annotationClass.getSimpleName() + " methods");
        }

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);
            if (method.getKind() != ElementKind.METHOD) {
                throw new UnsupportedOperationException("Element in " + typeElement.getQualifiedName() + " annotated with @" + annotationClass.getSimpleName() + " is not method");
            }
            if (method.getParameters().size() > 0) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @" + annotationClass.getSimpleName() + " can't have parameters");
            }
            if (method.getModifiers().contains(Modifier.STATIC)) {
                createControllable(controllableName, method);
                methodSpec.addStatement("$N.$N(() -> $T.$N())", controllableName.get(), controllableMethod, typeElement, method.getSimpleName());
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @" + annotationClass.getSimpleName() + " method because init method doesn't return any instance"
                    );
                }
                createControllable(controllableName, method);
                methodSpec.addStatement("$N.$N(() -> $N.$N())", controllableName.get(), controllableMethod, returnedName, method.getSimpleName());
            }
        }
    }

    private void createControllable(AtomicReference<String> controllableName, ExecutableElement method) {
        if (controllableName.get() == null) {
            var name = "genericControllable" + (index++);
            controllableName.set(name);
            methodSpec.addStatement("$T $N = this.$N.child()", ClassName.get("org.screamingsandals.lib.utils", "Controllable"), name, "pluginControllable");
        }
    }
}
