package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.annotations.methods.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
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
    private final Elements elements;
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
            put("org.screamingsandals.lib.plugin.PluginDescription", (statement, processedArguments) -> {
                statement.append("$N");
                processedArguments.add("description");
            });
            put("org.screamingsandals.lib.plugin.logger.LoggerWrapper", (statement, processedArguments) -> {
                statement.append("$N");
                processedArguments.add("screamingLogger");
            });
            put("org.slf4j.Logger", (statement, processedArguments) -> {
                // LoggerWrapper or lombok's @Slf4j should be used instead of this
                statement.append("($T) $N");
                processedArguments.add(ClassName.get("org.slf4j", "Logger"));
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

        if (initMethod.isEmpty() && !serviceContainer.isStaticOnly()) {
            initMethod = typeElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                    .findFirst();
        }

        if (initMethod.isPresent() || serviceContainer.isStaticOnly()) {
            String returnedName = null;
            if (initMethod.isPresent()) {
                var processedArguments = new ArrayList<>();
                var statement = new StringBuilder();
                var method = (ExecutableElement) initMethod.get();
                var arguments = method.getParameters();
                if (method.getKind() == ElementKind.CONSTRUCTOR || method.getReturnType().equals(typeElement.asType())) {
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
                    } else if ((typeMirror = instancedServices.keySet().stream().filter(type -> types.isAssignable(type, variableElement.asType())).findFirst()).isPresent()) {
                        statement.append("$N");
                        processedArguments.add(instancedServices.get(typeMirror.get()));
                    } else {
                        throw new UnsupportedOperationException("Init method of " + typeElement.getQualifiedName() + " has wrong argument!");
                    }
                });
                statement.append(")");
                methodSpec.addStatement(statement.toString(), processedArguments.toArray());
            }
            if (!serviceContainer.isStaticOnly() && returnedName != null) {
                methodSpec.addStatement("$T.$N($N)", ClassName.get("org.screamingsandals.lib.plugin", "ServiceManager"), "putService", returnedName);
                instancedServices.put(typeElement.asType(), returnedName);
            }

            var shouldRunControllable = processShouldRunControllable(typeElement, returnedName);

            var controllableForMethods = new AtomicReference<String>();

            processMethodAnnotation(
                    Map.of(
                            OnEnable.class, "enable",
                            OnPostEnable.class, "postEnable",
                            OnPreDisable.class, "preDisable",
                            OnDisable.class, "disable"
                    ),
                    typeElement,
                    returnedName,
                    controllableForMethods,
                    shouldRunControllable
            );

            processEventAnnotations(typeElement, returnedName, shouldRunControllable);
        } else {
            throw new UnsupportedOperationException("Can't auto initialize " + typeElement.getQualifiedName() + " without init method");
        }
    }

    private void processMethodAnnotation(Map<Class<? extends Annotation>, String> map, TypeElement typeElement, String returnedName, AtomicReference<String> controllableName, Pair<String, List<Object>> shouldRunControllable) {
        map.forEach((annotationClass, controllableMethod) ->
                processMethodAnnotation(annotationClass, controllableMethod, typeElement, returnedName, controllableName, shouldRunControllable)
        );
    }

    private void processMethodAnnotation(Class<? extends Annotation> annotationClass, String controllableMethod, TypeElement typeElement, String returnedName, AtomicReference<String> controllableName, Pair<String, List<Object>> shouldRunControllable) {
        var result1 = typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(annotationClass) != null)
                .collect(Collectors.toList());

        if (result1.size() > 1) {
            throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @" + annotationClass.getSimpleName() + " methods");
        }

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);
            if (method.getParameters().size() > 0) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @" + annotationClass.getSimpleName() + " can't have parameters");
            }
            var methodBuilder = MethodSpec.methodBuilder("run")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class);
            if (shouldRunControllable.areBothPresent()) {
                methodBuilder.beginControlFlow("if (" + shouldRunControllable.getFirst() + ")", shouldRunControllable.getSecond().toArray());
            }
            if (method.getModifiers().contains(Modifier.STATIC)) {
                createControllable(controllableName);
                methodBuilder.addStatement("$T.$N()", typeElement, method.getSimpleName());
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @" + annotationClass.getSimpleName() + " method because init method doesn't return any instance"
                    );
                }
                createControllable(controllableName);
                methodBuilder.addStatement("$N.$N()", returnedName, method.getSimpleName());
            }
            if (shouldRunControllable.areBothPresent()) {
                methodBuilder.endControlFlow();
            }
            methodSpec.addStatement(
                    "$N.$N($L)",
                    controllableName.get(),
                    controllableMethod,
                    TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(Runnable.class)
                            .addMethod(methodBuilder.build())
                            .build()
            );
        }
    }

    private Pair<String, List<Object>> processShouldRunControllable(TypeElement typeElement, String returnedName) {
        var result1 = typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(ShouldRunControllable.class) != null)
                .collect(Collectors.toList());

        if (result1.size() > 1) {
            throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @ShouldRunControllable methods");
        }

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);
            if (method.getParameters().size() > 0) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @ShouldRunControllable can't have parameters");
            }
            if (method.getReturnType().getKind() != TypeKind.BOOLEAN) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @ShouldRunControllable must return boolean");
            }
            if (method.getModifiers().contains(Modifier.STATIC)) {
                return Pair.of("$T.$N()", List.of(typeElement, method.getSimpleName()));
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @ShouldRunControllable method because init method doesn't return any instance"
                    );
                }
                return Pair.of("$N.$N()", List.of(returnedName, method.getSimpleName()));
            }
        }

        return Pair.empty();
    }


    private void processEventAnnotations(TypeElement typeElement, String returnedName, Pair<String, List<Object>> shouldRunControllable) {
        var result = typeElement.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(OnEvent.class) != null)
                .map(element -> (ExecutableElement) element)
                .collect(Collectors.toList());

        if (result.size() == 0) {
            return;
        }

        var abstractEventClass = elements.getTypeElement("org.screamingsandals.lib.event.AbstractEvent");
        var eventManagerClass = ClassName.get("org.screamingsandals.lib.event", "EventManager");
        var eventHandlerClass = ClassName.get("org.screamingsandals.lib.event", "EventHandler");
        var eventPriorityClass = ClassName.get("org.screamingsandals.lib.event", "EventPriority");

        var methodBuilder = MethodSpec.methodBuilder("run")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        if (shouldRunControllable.areBothPresent()) {
            methodBuilder.beginControlFlow("if (" + shouldRunControllable.getFirst() + ")", shouldRunControllable.getSecond().toArray());
        }

        result.forEach(method -> {
            if (method.getParameters().size() != 1) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @OnEvent must have one parameter");
            }
            var parameter = method.getParameters().get(0).asType();
            if (!types.isAssignable(parameter, abstractEventClass.asType())) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @OnEvent must have parameter that extends AbstractEvent");
            }
            var annotation = method.getAnnotation(OnEvent.class);
            if (method.getModifiers().contains(Modifier.STATIC)) {
                methodBuilder.addStatement(
                        "$T.getDefaultEventManager().register($T.class, $T.of(event -> $T.$N(event), $T.$N, $L))",
                        eventManagerClass,
                        parameter,
                        eventHandlerClass,
                        typeElement,
                        method.getSimpleName(),
                        eventPriorityClass,
                        annotation.priority().name(),
                        annotation.ignoreCancelled()
                );
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @OnEvent method because init method doesn't return any instance"
                    );
                }
                methodBuilder.addStatement(
                        "$T.getDefaultEventManager().register($T.class, $T.of(event -> $N.$N(event), $T.$N, $L))",
                        eventManagerClass,
                        parameter,
                        eventHandlerClass,
                        returnedName,
                        method.getSimpleName(),
                        eventPriorityClass,
                        annotation.priority().name(),
                        annotation.ignoreCancelled()
                );
            }
        });
        if (shouldRunControllable.areBothPresent()) {
            methodBuilder.endControlFlow();
        }

        methodSpec.addStatement(
                "this.$N.child().postEnable($L)",
                "pluginControllable",
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(Runnable.class)
                        .addMethod(methodBuilder.build())
                        .build()
        );
    }

    private void createControllable(AtomicReference<String> controllableName) {
        if (controllableName.get() == null) {
            var name = "genericControllable" + (index++);
            controllableName.set(name);
            methodSpec.addStatement("$T $N = this.$N.child()", ClassName.get("org.screamingsandals.lib.utils", "Controllable"), name, "pluginControllable");
        }
    }
}
