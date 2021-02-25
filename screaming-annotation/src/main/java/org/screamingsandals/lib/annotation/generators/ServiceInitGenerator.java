package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.methods.*;
import org.screamingsandals.lib.utils.annotations.parameters.ConfigFile;
import org.screamingsandals.lib.utils.annotations.parameters.DataFolder;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final Map<Triple<CheckType, String, Class<? extends Annotation>>, QuadConsumer<StringBuilder, List<Object>, Annotation, TypeElement>> annotatedInitArguments = new HashMap<>() {
        {
            put(Triple.of(CheckType.ONLY_SAME, "java.nio.file.Path", DataFolder.class), (statement, processedArguments, annotation, variableType) -> {
                var dataFolder = (DataFolder) annotation;
                statement.append("$N.getDataFolder()");
                processedArguments.add("description");
                if (!dataFolder.value().isEmpty()) {
                    statement.append(".resolve($S)");
                    processedArguments.add(dataFolder.value());
                }
            });
            put(Triple.of(CheckType.ONLY_SAME, "java.io.File", DataFolder.class), (statement, processedArguments, annotation, variableType) -> {
                var dataFolder = (DataFolder) annotation;
                statement.append("$N.getDataFolder()");
                processedArguments.add("description");
                if (!dataFolder.value().isEmpty()) {
                    statement.append(".resolve($S)");
                    processedArguments.add(dataFolder.value());
                }
                statement.append(".toFile()");
            });
            put(Triple.of(CheckType.ONLY_SAME, "java.nio.file.Path", ConfigFile.class), (statement, processedArguments, annotation, variableType) -> {
                var configFile = (ConfigFile) annotation;
                statement.append("$N.getDataFolder().resolve($S)");
                processedArguments.add("description");
                processedArguments.add(configFile.value());
            });
            put(Triple.of(CheckType.ONLY_SAME, "java.io.File", ConfigFile.class), (statement, processedArguments, annotation, variableType) -> {
                var configFile = (ConfigFile) annotation;
                statement.append("$N.getDataFolder().resolve($S).toFile()");
                processedArguments.add("description");
                processedArguments.add(configFile.value());
            });
            put(Triple.of(CheckType.ALLOW_CHILDREN, "org.spongepowered.configurate.loader.ConfigurationLoader", ConfigFile.class), (statement, processedArguments, annotation, variableType) -> {
                var configFile = (ConfigFile) annotation;
                if (!configFile.old().isEmpty()) {
                    var oldIndex = index++;
                    var newIndex = index++;

                    ServiceInitGenerator.this.methodSpec.addStatement("$T $N = $N.getDataFolder().resolve($S)", Path.class, "indexedVariable" + oldIndex, "description", configFile.old());
                    ServiceInitGenerator.this.methodSpec.addStatement("$T $N = $N.getDataFolder().resolve($S)", Path.class, "indexedVariable" + newIndex, "description", configFile.value());

                    ServiceInitGenerator.this.methodSpec
                            .beginControlFlow("if ($T.exists($N) && !$T.exists($N))", Files.class, "indexedVariable" + oldIndex, Files.class, "indexedVariable" + newIndex)
                                .beginControlFlow("try")
                                    .addStatement("$T.move($N, $N)", Files.class, "indexedVariable" + oldIndex, "indexedVariable" + newIndex)
                                .nextControlFlow("catch ($T $N)", Exception.class, "ex")
                                    .addStatement("$N.printStackTrace()", "ex")
                                .endControlFlow()
                            .endControlFlow();
                }

                statement.append("$T.builder().path($N.getDataFolder().resolve($S))");
                processedArguments.add(variableType);
                processedArguments.add("description");
                processedArguments.add(configFile.value());
                variableType.getEnclosedElements()
                        .stream()
                        .filter(element -> element.getKind() == ElementKind.CLASS && element.getSimpleName().toString().contains("Builder"))
                        .findFirst()
                        .flatMap(builder -> builder
                                .getEnclosedElements()
                                .stream()
                                .filter(element -> element.getKind() == ElementKind.METHOD && element.getSimpleName().contentEquals("nodeStyle"))
                                .map(element -> (ExecutableElement) element)
                                .findFirst()
                        )
                        .ifPresent(element -> {
                            var parameters = element.getParameters();
                            if (parameters.size() == 1) {
                                statement.append(".nodeStyle($T.$N)");
                                processedArguments.add(parameters.get(0).asType());
                                processedArguments.add("BLOCK");
                            }
                        });
                statement.append(".build()");
            });
        }
    };
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
            put("org.screamingsandals.lib.utils.logger.LoggerWrapper", (statement, processedArguments) -> {
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

    @SuppressWarnings("unchecked")
    public <A extends Annotation> ServiceInitGenerator add(CheckType type, String name, Class<A> annotationClass, QuadConsumer<StringBuilder, List<Object>, A, TypeElement> initArgGen) {
        annotatedInitArguments.put(Triple.of(type, name, annotationClass), (QuadConsumer<StringBuilder, List<Object>, Annotation, TypeElement>) initArgGen);
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
                    var annotatedInitArgument = annotatedInitArguments.entrySet().stream()
                            .filter(entry -> entry.getKey().getFirst().function
                                    .apply(
                                            types,
                                            variableElement.asType(),
                                            types.erasure(elements.getTypeElement(entry.getKey().getSecond()).asType())
                                    )
                                    && variableElement.getAnnotation(entry.getKey().getThird()) != null
                            )
                            .findFirst();
                    Element annotatedElement = variableElement;
                    if (annotatedInitArgument.isEmpty()) {
                        // probably lombok, try other thing
                        var lombokedElement = typeElement.getEnclosedElements().stream()
                                .filter(element -> element.getKind().isField()
                                        && element.getSimpleName().contentEquals(variableElement.getSimpleName())
                                        && types.isSameType(element.asType(), variableElement.asType())
                                )
                                .findFirst();

                        if (lombokedElement.isPresent()) {
                            annotatedElement = lombokedElement.get();
                            annotatedInitArgument = annotatedInitArguments.entrySet().stream()
                                    .filter(entry -> entry.getKey().getFirst().function
                                            .apply(
                                                    types,
                                                    variableElement.asType(),
                                                    types.erasure(elements.getTypeElement(entry.getKey().getSecond()).asType())
                                            )
                                            && lombokedElement.get().getAnnotation(entry.getKey().getThird()) != null
                                    )
                                    .findFirst();
                        }

                    }

                    if (annotatedInitArgument.isPresent()) {
                        annotatedInitArgument.get().getValue()
                                .accept(
                                        statement,
                                        processedArguments,
                                        annotatedElement.getAnnotation(annotatedInitArgument.get().getKey().getThird()),
                                        (TypeElement) types.asElement(variableElement.asType())
                                );
                    } else if (initArguments.containsKey(variableElement.asType().toString())) {
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

    @RequiredArgsConstructor
    public enum CheckType {
        ONLY_SAME((types, typeMirror, typeMirror2) -> {
            System.out.println(typeMirror.toString() + " is " + typeMirror2.toString() + " = " + types.isSameType(typeMirror2, typeMirror));
            return types.isSameType(typeMirror2, typeMirror);
        }),
        ALLOW_SUPERCLASS((types, typeMirror, typeMirror2) -> {
            System.out.println(typeMirror2.toString() + " into " + typeMirror.toString() + " = " + types.isAssignable(typeMirror2, typeMirror));
            return types.isAssignable(typeMirror2, typeMirror);
        }),
        ALLOW_CHILDREN((types, typeMirror, typeMirror2) -> {
            System.out.println(typeMirror.toString() + " into " + typeMirror2.toString() + " = " + types.isSubtype(typeMirror, typeMirror2));
            return types.isSubtype(typeMirror, typeMirror2);
        });

        private final TriFunction<Types, TypeMirror, TypeMirror, Boolean> function;
    }
}
