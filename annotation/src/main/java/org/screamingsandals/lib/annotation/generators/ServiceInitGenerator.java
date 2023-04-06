/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.annotation.constants.Classes;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.QuadConsumer;
import org.screamingsandals.lib.utils.TriFunction;
import org.screamingsandals.lib.utils.Triple;
import org.screamingsandals.lib.utils.annotations.internal.PlatformPluginObject;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPluginLoad;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.utils.annotations.methods.Provider;
import org.screamingsandals.lib.utils.annotations.methods.ShouldRunControllable;
import org.screamingsandals.lib.utils.annotations.parameters.ConfigFile;
import org.screamingsandals.lib.utils.annotations.parameters.DataFolder;
import org.screamingsandals.lib.utils.annotations.parameters.ProvidedBy;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ServiceInitGenerator {
    private long index;

    @Getter
    private final @NotNull String platformClassName;
    private final CodeBlock.@NotNull Builder methodSpec;
    private final @NotNull Types types;
    private final @NotNull Elements elements;
    private final @NotNull Map<@NotNull TypeMirror, String> instancedServices = new HashMap<>();
    private final @NotNull Queue<@NotNull Pair<@NotNull ServiceContainer, @Nullable String>> delayedControllables = new LinkedList<>();
    private final @NotNull Map<@NotNull Pair<@NotNull TypeElement, @NotNull TypeElement>, Pair<Provider.Level, String>> providers = new HashMap<>();
    private final @NotNull Map<@NotNull Triple<@NotNull CheckType, @NotNull String, @NotNull Class<? extends Annotation>>, QuadConsumer<@NotNull StringBuilder, @NotNull List<Object>, @NotNull Annotation, @NotNull TypeElement>> annotatedInitArguments = new HashMap<>();
    private final @NotNull Map<@NotNull String, BiConsumer<@NotNull StringBuilder, @NotNull List<@NotNull Object>>> initArguments = new HashMap<>();

    @Getter
    private boolean requiredDescription;
    @Getter
    private boolean requiredScreamingLogger;
    @Getter
    private boolean requiredSlf4jLogger;

    public ServiceInitGenerator(@NotNull String platformClassName, CodeBlock.@NotNull Builder methodSpec, @NotNull Types types, @NotNull Elements elements) {
        this.platformClassName = platformClassName;
        this.methodSpec = methodSpec;
        this.types = types;
        this.elements = elements;

        // default annotated parameters

        add(CheckType.ONLY_SAME, Classes.Java.NIO_PATH.canonicalName(), DataFolder.class, (statement, processedArguments, annotation, variableType) -> {
            statement.append("$N.getDataFolder()");
            processedArguments.add("description");
            if (!annotation.value().isEmpty()) {
                statement.append(".resolve($S)");
                processedArguments.add(annotation.value());
            }
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(CheckType.ONLY_SAME, Classes.Java.FILE.canonicalName(), DataFolder.class, (statement, processedArguments, annotation, variableType) -> {
            statement.append("$N.getDataFolder()");
            processedArguments.add("description");
            if (!annotation.value().isEmpty()) {
                statement.append(".resolve($S)");
                processedArguments.add(annotation.value());
            }
            statement.append(".toFile()");
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(CheckType.ONLY_SAME, Classes.Java.NIO_PATH.canonicalName(), ConfigFile.class, (statement, processedArguments, annotation, variableType) -> {
            statement.append("$N.getDataFolder().resolve($S)");
            processedArguments.add("description");
            processedArguments.add(annotation.value());
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(CheckType.ONLY_SAME, Classes.Java.FILE.canonicalName(), ConfigFile.class, (statement, processedArguments, annotation, variableType) -> {
            statement.append("$N.getDataFolder().resolve($S).toFile()");
            processedArguments.add("description");
            processedArguments.add(annotation.value());
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(CheckType.ALLOW_CHILDREN, Classes.Configurate.CONFIGURATION_LOADER.canonicalName(), ConfigFile.class, (statement, processedArguments, annotation, variableType) -> {
            if (!annotation.old().isEmpty()) {
                var oldIndex = index++;
                var newIndex = index++;

                ServiceInitGenerator.this.methodSpec.addStatement("$T $N = $N.getDataFolder().resolve($S)", Path.class, "indexedVariable" + oldIndex, "description", annotation.old());
                ServiceInitGenerator.this.methodSpec.addStatement("$T $N = $N.getDataFolder().resolve($S)", Path.class, "indexedVariable" + newIndex, "description", annotation.value());

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
            processedArguments.add(annotation.value());
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
            if (annotation.screamingLibSerializers()) {
                if (ServiceInitGenerator.this.elements.getTypeElement(Classes.SLib.SERIALIZERS.canonicalName()) != null) { // only for Core (includes Spectator serializers as well)
                    statement.append(".defaultOptions(t -> t.serializers($T::makeSerializers))");
                    processedArguments.add(Classes.SLib.SERIALIZERS);
                } else if (ServiceInitGenerator.this.elements.getTypeElement(Classes.SLib.SPECTATOR_SERIALIZERS.canonicalName()) != null) { // Proxies currently have only spectator serializers
                    statement.append(".defaultOptions(t -> t.serializers($T::makeSerializers))");
                    processedArguments.add(Classes.SLib.SPECTATOR_SERIALIZERS);
                }
            }
            statement.append(".build()");
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(CheckType.ALLOW_CHILDREN, Classes.Java.OBJECT.canonicalName(), ProvidedBy.class, (statement, processedArguments, annotation, variableType) -> {
            var providerClass = MiscUtils.getSafelyTypeElement(ServiceInitGenerator.this.types, annotation);
            var provider = Pair.of(providerClass, variableType);
            var provided = providers.get(provider);
            if (provided != null) {
                if (provided.getFirst() == Provider.Level.INIT) {
                    statement.append("($T) $N");
                } else {
                    statement.append("($T) $N.get()");
                }
                processedArguments.add(toRawClassName(variableType)); // fucking generics
                processedArguments.add(provided.getSecond());
            } else {
                throw new UnsupportedOperationException("Can't provide from " + providerClass  + "! Are you sure every dependencies are configured correctly?");
            }
        });
        add(CheckType.ONLY_SAME, Classes.Java.OBJECT.canonicalName(), PlatformPluginObject.class, (statement, processedArguments, annotation, variableType) -> {
            statement.append(ServiceInitGenerator.this.platformClassName).append(".this");
        });

        // default parameters

        add(Classes.SLib.CONTROLLABLE_IMPL.canonicalName(), (statement, processedArguments) -> {
            statement.append(ServiceInitGenerator.this.platformClassName).append(".this.$N.child()");
            processedArguments.add("pluginControllable");
        });
        add(Classes.SLib.CONTROLLABLE.canonicalName(), (statement, processedArguments) -> {
            statement.append(ServiceInitGenerator.this.platformClassName).append(".this.$N.child()");
            processedArguments.add("pluginControllable");
        });
        add(Classes.SLib.PLUGIN.canonicalName(), (statement, processedArguments) -> {
            statement.append("$N");
            processedArguments.add("description");
            ServiceInitGenerator.this.requiredDescription = true;
        });
        add(Classes.SLib.LOGGER_WRAPPER.canonicalName(), (statement, processedArguments) -> {
            statement.append("$N");
            processedArguments.add("screamingLogger");
            ServiceInitGenerator.this.requiredScreamingLogger = true;
        });
        add(Classes.Slf4j.LOGGER.canonicalName(), (statement, processedArguments) -> {
            // LoggerWrapper or lombok's @Slf4j should be used instead of this
            statement.append("($T) $N");
            processedArguments.add(Classes.Slf4j.LOGGER);
            processedArguments.add("slf4jLogger");
            ServiceInitGenerator.this.requiredSlf4jLogger = true;
        });
    }

    public @NotNull ServiceInitGenerator add(@NotNull String name, @NotNull BiConsumer<@NotNull StringBuilder, @NotNull List<Object>> initArgGen) {
        return add(List.of(name), initArgGen);
    }

    public @NotNull ServiceInitGenerator add(@NotNull List<@NotNull String> names, @NotNull BiConsumer<@NotNull StringBuilder, @NotNull List<Object>> initArgGen) {
        names.forEach(s -> initArguments.put(s, initArgGen));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> @NotNull ServiceInitGenerator add(@NotNull CheckType type, @NotNull String name, @NotNull Class<A> annotationClass, @NotNull QuadConsumer<@NotNull StringBuilder, @NotNull List<Object>, @NotNull A, @NotNull TypeElement> initArgGen) {
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
                if (method.getKind() == ElementKind.CONSTRUCTOR || types.isSameType(method.getReturnType(), typeElement.asType())) {
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
            var postConstruct = processOnPostConstruct(typeElement, returnedName);
            if (postConstruct.areBothPresent()) {
                methodSpec.addStatement(postConstruct.first(), postConstruct.second().toArray());
            }

            if (!serviceContainer.isStaticOnly() && returnedName != null) {
                methodSpec.addStatement("$T.$N($N)", Classes.SLib.SERVICE_MANAGER, "putService", returnedName);
                instancedServices.put(typeElement.asType(), returnedName);
            }

            if (serviceContainer.isDelayControllables()) {
                delayedControllables.add(Pair.of(serviceContainer, returnedName));
                return;
            }

            processControllablesOfService(serviceContainer, returnedName);

        } else {
            throw new UnsupportedOperationException("Can't auto initialize " + typeElement.getQualifiedName() + " without init method");
        }
    }

    public void processDelayedControllables() {
        Pair<@NotNull ServiceContainer, @Nullable String> pair;
        while ((pair = delayedControllables.poll()) != null) {
            processControllablesOfService(pair.first(), pair.second());
        }
    }

    private void processControllablesOfService(@NotNull ServiceContainer serviceContainer, @Nullable String returnedName) {
        var typeElement = serviceContainer.getService();

        var shouldRunControllable = processShouldRunControllable(typeElement, returnedName);

        var controllableForMethods = new AtomicReference<String>();

        processMethodAnnotation(
                Map.of(
                        OnPluginLoad.class, "pluginLoad",
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

        processProviders(typeElement, returnedName, shouldRunControllable);

        processEventAnnotations(typeElement, returnedName, shouldRunControllable);
    }

    private void processMethodAnnotation(Map<Class<? extends Annotation>, String> map, TypeElement typeElement, String returnedName, AtomicReference<String> controllableName, Pair<String, List<Object>> shouldRunControllable) {
        map.forEach((annotationClass, controllableMethod) ->
                processMethodAnnotation(annotationClass, controllableMethod, typeElement, returnedName, controllableName, shouldRunControllable)
        );
    }

    private void processMethodAnnotation(Class<? extends Annotation> annotationClass, String controllableMethod, TypeElement typeElement, String returnedName, AtomicReference<String> controllableName, Pair<String, List<Object>> shouldRunControllable) {
        var superClass = typeElement;
        List<Element> result1;
        do {
            result1 = superClass
                    .getEnclosedElements().stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD
                            && element.getAnnotation(annotationClass) != null
                            && element.getModifiers().contains(Modifier.PUBLIC)
                    )
                    .collect(Collectors.toList());

            if (result1.size() > 1) {
                throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @" + annotationClass.getSimpleName() + " methods");
            }

        } while (result1.isEmpty() && (superClass = (TypeElement) types.asElement(superClass.getSuperclass())) != null);

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);

            var processedArguments = new ArrayList<>();
            var statement = new StringBuilder();
            var arguments = method.getParameters();
            if (method.getModifiers().contains(Modifier.STATIC)) {
                statement.append("$T.$N(");
                processedArguments.add(typeElement);
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @" + annotationClass.getSimpleName() + " method because init method doesn't return any instance"
                    );
                }
                statement.append("$N.$N(");
                processedArguments.add(returnedName);
            }
            processedArguments.add(method.getSimpleName());
            var first = new AtomicBoolean(true);
            arguments.forEach(variableElement -> {
                if (!first.get()) {
                    statement.append(",");
                } else {
                    first.set(false);
                }
                Optional<TypeMirror> typeMirror;
                var annotatedParameter = annotatedInitArguments.entrySet().stream()
                        .filter(entry -> entry.getKey().getFirst().function
                                .apply(
                                        types,
                                        variableElement.asType(),
                                        types.erasure(elements.getTypeElement(entry.getKey().getSecond()).asType())
                                )
                                && variableElement.getAnnotation(entry.getKey().getThird()) != null
                        )
                        .findFirst();

                if (annotatedParameter.isPresent()) {
                    annotatedParameter.get().getValue()
                            .accept(
                                    statement,
                                    processedArguments,
                                    variableElement.getAnnotation(annotatedParameter.get().getKey().getThird()),
                                    (TypeElement) types.asElement(variableElement.asType())
                            );
                } else if (initArguments.containsKey(variableElement.asType().toString())) {
                    initArguments.get(variableElement.asType().toString()).accept(statement, processedArguments);
                } else if ((typeMirror = instancedServices.keySet().stream().filter(type -> types.isAssignable(type, variableElement.asType())).findFirst()).isPresent()) {
                    statement.append("$N");
                    processedArguments.add(instancedServices.get(typeMirror.get()));
                } else {
                    throw new UnsupportedOperationException("Method " +   method.getSimpleName() + " of " + typeElement.getQualifiedName() + " has wrong argument!");
                }
            });
            statement.append(")");

            var methodBuilder = CodeBlock.builder().beginControlFlow("() -> "); // lambda block
            if (shouldRunControllable.areBothPresent()) {
                methodBuilder.beginControlFlow("if (" + shouldRunControllable.getFirst() + ")", shouldRunControllable.getSecond().toArray());
            }
            createControllable(controllableName);
            methodBuilder.addStatement(statement.toString(), processedArguments.toArray());
            if (shouldRunControllable.areBothPresent()) {
                methodBuilder.endControlFlow();
            }
            methodSpec.addStatement(
                    "$N.$N($L)",
                    controllableName.get(),
                    controllableMethod,
                    methodBuilder.endControlFlow().build().toString()
            );
        }
    }

    private Pair<String, List<Object>> processShouldRunControllable(TypeElement typeElement, String returnedName) {
        var superClass = typeElement;
        List<Element> result1;
        do {
            result1 = superClass
                    .getEnclosedElements().stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD
                            && element.getAnnotation(ShouldRunControllable.class) != null
                            && element.getModifiers().contains(Modifier.PUBLIC)
                    )
                    .collect(Collectors.toList());

            if (result1.size() > 1) {
                throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @ShouldRunControllable methods");
            }

        } while (result1.isEmpty() && (superClass = (TypeElement) types.asElement(superClass.getSuperclass())) != null);

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);
            if (!method.getParameters().isEmpty()) {
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
    private Pair<String, List<Object>> processOnPostConstruct(TypeElement typeElement, String returnedName) {
        var superClass = typeElement;
        List<Element> result1;
        do {
            result1 = superClass
                    .getEnclosedElements().stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD
                            && element.getAnnotation(OnPostConstruct.class) != null
                            && element.getModifiers().contains(Modifier.PUBLIC)
                    )
                    .collect(Collectors.toList());

            if (result1.size() > 1) {
                throw new UnsupportedOperationException("Service " + typeElement.getQualifiedName() + " has more than one @OnPostConstruct methods");
            }

        } while (result1.isEmpty() && (superClass = (TypeElement) types.asElement(superClass.getSuperclass())) != null);

        if (result1.size() == 1) {
            var method = (ExecutableElement) result1.get(0);

            var processedArguments = new ArrayList<>();
            var statement = new StringBuilder();
            var arguments = method.getParameters();
            if (method.getModifiers().contains(Modifier.STATIC)) {
                statement.append("$T.$N(");
                processedArguments.add(typeElement);
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @OnPostConstruct method because init method doesn't return any instance"
                    );
                }
                statement.append("$N.$N(");
                processedArguments.add(returnedName);
            }
            processedArguments.add(method.getSimpleName());
            var first = new AtomicBoolean(true);
            arguments.forEach(variableElement -> {
                if (!first.get()) {
                    statement.append(",");
                } else {
                    first.set(false);
                }
                Optional<TypeMirror> typeMirror;
                var annotatedParameter = annotatedInitArguments.entrySet().stream()
                        .filter(entry -> entry.getKey().getFirst().function
                                .apply(
                                        types,
                                        variableElement.asType(),
                                        types.erasure(elements.getTypeElement(entry.getKey().getSecond()).asType())
                                )
                                && variableElement.getAnnotation(entry.getKey().getThird()) != null
                        )
                        .findFirst();

                if (annotatedParameter.isPresent()) {
                    annotatedParameter.get().getValue()
                            .accept(
                                    statement,
                                    processedArguments,
                                    variableElement.getAnnotation(annotatedParameter.get().getKey().getThird()),
                                    (TypeElement) types.asElement(variableElement.asType())
                            );
                } else if (initArguments.containsKey(variableElement.asType().toString())) {
                    initArguments.get(variableElement.asType().toString()).accept(statement, processedArguments);
                } else if ((typeMirror = instancedServices.keySet().stream().filter(type -> types.isAssignable(type, variableElement.asType())).findFirst()).isPresent()) {
                    statement.append("$N");
                    processedArguments.add(instancedServices.get(typeMirror.get()));
                } else {
                    throw new UnsupportedOperationException("Method " +   method.getSimpleName() + " of " + typeElement.getQualifiedName() + " has wrong argument!");
                }
            });
            statement.append(")");

            return Pair.of(statement.toString(), processedArguments);
        }

        return Pair.empty();
    }

    private void processProviders(TypeElement typeElement, String returnedName, Pair<String, List<Object>> shouldRunControllable) {
        List<ExecutableElement> result = new ArrayList<>();

        var superClass = typeElement;
        do {
            result.addAll(superClass
                    .getEnclosedElements().stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD
                            && element.getAnnotation(Provider.class) != null
                            && element.getModifiers().contains(Modifier.PUBLIC)
                    )
                    .map(element -> (ExecutableElement) element)
                    .collect(Collectors.toList()));
        } while ((superClass = (TypeElement) types.asElement(superClass.getSuperclass())) != null);

        result = result
                .stream()
                .filter(distinctByKey(element -> Pair.of(
                        element.getSimpleName(),
                        element.getParameters()
                                .stream()
                                .map(variableElement -> variableElement.asType().toString())
                                .collect(Collectors.toList()))
                ))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return;
        }

        result.forEach(method -> {
            var annotation = method.getAnnotation(Provider.class);
            if (method.getReturnType() instanceof NoType) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": Method annotated with @Provider must return something");
            }
            var returnType = method.getReturnType();

            var processedArguments = new ArrayList<>();
            var statement = new StringBuilder();
            var arguments = method.getParameters();
            if (method.getModifiers().contains(Modifier.STATIC)) {
                statement.append("$T.$N(");
                processedArguments.add(typeElement);
            } else {
                if (returnedName == null) {
                    throw new UnsupportedOperationException(
                            typeElement.getQualifiedName() + ": Can't dynamically add non-static @Provider method because init method doesn't return any instance"
                    );
                }
                statement.append("$N.$N(");
                processedArguments.add(returnedName);
            }
            processedArguments.add(method.getSimpleName());
            var first = new AtomicBoolean(true);
            arguments.forEach(variableElement -> {
                if (!first.get()) {
                    statement.append(",");
                } else {
                    first.set(false);
                }
                Optional<TypeMirror> typeMirror;
                var annotatedParameter = annotatedInitArguments.entrySet().stream()
                        .filter(entry -> entry.getKey().getFirst().function
                                .apply(
                                        types,
                                        variableElement.asType(),
                                        types.erasure(elements.getTypeElement(entry.getKey().getSecond()).asType())
                                )
                                && variableElement.getAnnotation(entry.getKey().getThird()) != null
                        )
                        .findFirst();

                if (annotatedParameter.isPresent()) {
                    annotatedParameter.get().getValue()
                            .accept(
                                    statement,
                                    processedArguments,
                                    variableElement.getAnnotation(annotatedParameter.get().getKey().getThird()),
                                    (TypeElement) types.asElement(variableElement.asType())
                            );
                } else if (initArguments.containsKey(variableElement.asType().toString())) {
                    initArguments.get(variableElement.asType().toString()).accept(statement, processedArguments);
                } else if ((typeMirror = instancedServices.keySet().stream().filter(type -> types.isAssignable(type, variableElement.asType())).findFirst()).isPresent()) {
                    statement.append("$N");
                    processedArguments.add(instancedServices.get(typeMirror.get()));
                } else {
                    throw new UnsupportedOperationException("Method " +   method.getSimpleName() + " of " + typeElement.getQualifiedName() + " has wrong argument!");
                }
            });
            statement.append(")");

            var indexv = "indexedVariable" + (index++);
            if (annotation.level() == Provider.Level.INIT) {
                var list = new ArrayList<>();
                list.add(returnType);
                list.add(indexv);
                list.addAll(processedArguments);
                methodSpec.addStatement("$T $N = " + statement, list.toArray());
                providers.put(Pair.of(typeElement, (TypeElement) types.asElement(returnType)), Pair.of(Provider.Level.INIT, indexv));
            } else if (annotation.level() == Provider.Level.ENABLE) {
                methodSpec.addStatement("$T $N = new $T()", AtomicReference.class, indexv, AtomicReference.class);
                var methodBuilder = CodeBlock.builder().beginControlFlow("() -> "); // lambda block
                if (shouldRunControllable.areBothPresent()) {
                    methodBuilder.beginControlFlow("if (" + shouldRunControllable.getFirst() + ")", shouldRunControllable.getSecond().toArray());
                }
                var list = new ArrayList<>();
                list.add(indexv);
                list.addAll(processedArguments);
                methodBuilder.addStatement("$N.set(" + statement + ")", list.toArray());
                if (shouldRunControllable.areBothPresent()) {
                    methodBuilder.endControlFlow();
                }
                methodSpec.addStatement(
                        "this.$N.child().enable($L)",
                        "pluginControllable",
                        methodBuilder.endControlFlow().build().toString()
                );
                providers.put(Pair.of(typeElement, (TypeElement) types.asElement(returnType)), Pair.of(Provider.Level.ENABLE, indexv));
            } else if (annotation.level() == Provider.Level.POST_ENABLE) {
                methodSpec.addStatement("$T $N = new $T()", AtomicReference.class, indexv, AtomicReference.class);
                var methodBuilder = CodeBlock.builder().beginControlFlow("() -> "); // lambda block
                if (shouldRunControllable.areBothPresent()) {
                    methodBuilder.beginControlFlow("if (" + shouldRunControllable.getFirst() + ")", shouldRunControllable.getSecond().toArray());
                }
                var list = new ArrayList<>();
                list.add(indexv);
                list.addAll(processedArguments);
                methodBuilder.addStatement("$N.set(" + statement + ")", list.toArray());
                if (shouldRunControllable.areBothPresent()) {
                    methodBuilder.endControlFlow();
                }
                methodSpec.addStatement(
                        "this.$N.child().postEnable($L)",
                        "pluginControllable",
                        methodBuilder.endControlFlow().build().toString()
                );
                providers.put(Pair.of(typeElement, (TypeElement)  types.asElement(returnType)), Pair.of(Provider.Level.POST_ENABLE, indexv));
            }
        });

    }

    private void processEventAnnotations(TypeElement typeElement, String returnedName, Pair<String, List<Object>> shouldRunControllable) {
        List<ExecutableElement> result = new ArrayList<>();

        var superClass = typeElement;
        do {
            result.addAll(superClass
                    .getEnclosedElements().stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD
                            && element.getAnnotation(OnEvent.class) != null
                            && element.getModifiers().contains(Modifier.PUBLIC)
                    )
                    .map(element -> (ExecutableElement) element)
                    .collect(Collectors.toList()));
        } while ((superClass = (TypeElement) types.asElement(superClass.getSuperclass())) != null);

        result = result
                .stream()
                .filter(distinctByKey(element -> Pair.of(
                        element.getSimpleName(),
                        element.getParameters()
                                .stream()
                                .map(variableElement -> variableElement.asType().toString())
                                .collect(Collectors.toList()))
                ))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return;
        }

        var abstractEventClass = elements.getTypeElement(Classes.SLib.EVENT.canonicalName());
        var eventManagerClass = Classes.SLib.EVENT_MANAGER;
        var eventHandlerClass = Classes.SLib.EVENT_HANDLER;
        var eventPriorityClass = Classes.SLib.EVENT_PRIORITY;

        var methodBuilder = CodeBlock.builder().beginControlFlow("() -> "); // lambda block
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
                methodBuilder.endControlFlow().build().toString()
        );
    }

    private void createControllable(AtomicReference<String> controllableName) {
        if (controllableName.get() == null) {
            var name = "genericControllable" + (index++);
            controllableName.set(name);
            methodSpec.addStatement("$T $N = this.$N.child()", Classes.SLib.CONTROLLABLE, name, "pluginControllable");
        }
    }

    public static <T> @NotNull Predicate<T> distinctByKey(@NotNull Function<? super T, ?> keyExtractor) {
        var seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static @NotNull ClassName toRawClassName(@NotNull TypeElement typeElement) {
        var list = new ArrayList<String>();
        list.add(typeElement.getSimpleName().toString());
        Element cur = typeElement;
        while (true) {
            cur = cur.getEnclosingElement();
            if (cur instanceof TypeElement) {
                list.add(cur.getSimpleName().toString());
            } else if (cur instanceof PackageElement) {
                list.add(((PackageElement) cur).getQualifiedName().toString());
                break;
            }
        }

        Collections.reverse(list);
        return ClassName.get(list.get(0), list.get(1), list.stream().skip(2).toArray(String[]::new));
    }

    @RequiredArgsConstructor
    public enum CheckType {
        ONLY_SAME(Types::isSameType),
        ALLOW_SUPERCLASS((types, typeMirror, typeMirror2) -> types.isAssignable(typeMirror2, typeMirror)),
        ALLOW_CHILDREN(Types::isSubtype);

        private final @NotNull TriFunction<@NotNull Types, @NotNull TypeMirror, @NotNull TypeMirror, @NotNull Boolean> function;
    }
}
