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

package org.screamingsandals.lib.annotation.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.*;
import org.screamingsandals.lib.utils.annotations.internal.AccessPluginClasses;
import org.screamingsandals.lib.utils.annotations.internal.InternalCoreService;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;
import org.screamingsandals.lib.utils.annotations.parameters.ProvidedBy;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class MiscUtils {
    private static final @Nullable Class<? extends Annotation> LOMBOK_UTILITY_CLASS;

    static {
        Class<? extends Annotation> lombok = null;
        try {
            //noinspection unchecked
            lombok = (Class<? extends Annotation>) Class.forName("lombok.experimental.UtilityClass");
        } catch (Throwable ignored) {
        }
        LOMBOK_UTILITY_CLASS = lombok;
    }

    public static TypeElement getSafelyTypeElement(Types typeUtils, ProvidedBy annotation) {
        try {
            annotation.value();
        } catch (MirroredTypeException mte) {
            return (TypeElement) typeUtils.asElement(mte.getTypeMirror());
        }
        return null;
    }

    public static List<TypeElement> getSafelyTypeElements(ProcessingEnvironment environment, ServiceDependencies annotation) {
        try {
            annotation.dependsOn();
        } catch (MirroredTypesException mte) {
            var typeUtils = environment.getTypeUtils();
            return mte.getTypeMirrors()
                    .stream()
                    .map(typeMirror -> (TypeElement) typeUtils.asElement(typeMirror))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static List<TypeElement> getSafelyTypeElementsInit(ProcessingEnvironment environment, ServiceDependencies annotation) {
        try {
            annotation.initAnother();
        } catch (MirroredTypesException mte) {
            var typeUtils = environment.getTypeUtils();
            return mte.getTypeMirrors()
                    .stream()
                    .map(typeMirror -> (TypeElement) typeUtils.asElement(typeMirror))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static List<TypeElement> getSafelyTypeElementsLoadAfter(ProcessingEnvironment environment, ServiceDependencies annotation) {
        try {
            annotation.loadAfter();
        } catch (MirroredTypesException mte) {
            var typeUtils = environment.getTypeUtils();
            return mte.getTypeMirrors()
                    .stream()
                    .map(typeMirror -> (TypeElement) typeUtils.asElement(typeMirror))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static List<TypeElement> getSafelyTypeElements(ProcessingEnvironment environment, Init annotation) {
        try {
            annotation.services();
        } catch (MirroredTypesException mte) {
            var typeUtils = environment.getTypeUtils();
            return mte.getTypeMirrors()
                    .stream()
                    .map(typeMirror -> (TypeElement) typeUtils.asElement(typeMirror))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static Map<PlatformType, ServiceContainer> getAllSpecificPlatformImplementations(ProcessingEnvironment environment, TypeElement typeElement, List<PlatformType> platformTypes, boolean strict) {
        var abstractService = typeElement.getAnnotation(ProvidedService.class);
        if (abstractService != null) {
            if (typeElement.getModifiers().contains(Modifier.FINAL)) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": A service annotated with @ProvidedService cannot have final modifier on it!");
            }

            if (typeElement.getAnnotation(Service.class) != null || typeElement.getAnnotation(AbstractService.class) != null) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": @ProvidedService annotation cannot be mixed with @Service or @AbstractService annotation!");
            }

            if (LOMBOK_UTILITY_CLASS != null && typeElement.getAnnotation(LOMBOK_UTILITY_CLASS) != null) {
                throw new UnsupportedOperationException(typeElement.getQualifiedName() + ": @ProvidedService annotation cannot be mixed with @lombok.experimental.UtilityClass annotation!");
            }

            var container = new ServiceContainer(
                    environment.getTypeUtils(),
                    typeElement,
                    typeElement.getAnnotation(InternalEarlyInitialization.class) != null,
                    false,
                    typeElement.getAnnotation(InternalCoreService.class) != null,
                    true,
                    false
            );

            return platformTypes
                    .stream()
                    .collect(Collectors.toMap(s -> s, o -> container));
        }

        var mappingAnnotation = typeElement.getAnnotation(AbstractService.class);
        if (mappingAnnotation == null) {
            var service = typeElement.getAnnotation(Service.class);
            if (service != null) {
                var container = new ServiceContainer(
                        environment.getTypeUtils(),
                        typeElement,
                        typeElement.getAnnotation(InternalEarlyInitialization.class) != null,
                        service.staticOnly() || (LOMBOK_UTILITY_CLASS != null && typeElement.getAnnotation(LOMBOK_UTILITY_CLASS) != null)
                                || (typeElement.getKind() == ElementKind.CLASS && typeElement.getEnclosedElements().stream()
                                .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                                .allMatch(element -> element.getModifiers().contains(Modifier.PRIVATE))
                                && typeElement.getEnclosedElements().stream().noneMatch(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(ServiceInitializer.class) != null)),
                        typeElement.getAnnotation(InternalCoreService.class) != null,
                        false,
                        LOMBOK_UTILITY_CLASS != null && typeElement.getAnnotation(LOMBOK_UTILITY_CLASS) != null
                );
                checkServiceDependencies(environment, typeElement, container);
                checkConstructorDependencies(environment, typeElement, container);
                checkEventManagerRequirement(environment, typeElement, container);
                checkAccessedPlugins(environment, typeElement, container);

                return platformTypes
                        .stream()
                        .collect(Collectors.toMap(s -> s, o -> container));
            }
            throw new UnsupportedOperationException("Type " + typeElement.getQualifiedName() + " doesn't have @AbstractService annotation");
        }

        var map = new HashMap<PlatformType, ServiceContainer>();

        final var rule = mappingAnnotation.value();

        platformTypes.forEach(platformType -> {
            var resolvedClassName = rule
                    .replaceAll("\\{platform}", platformType.name().toLowerCase(Locale.ROOT))
                    .replaceAll("\\{Platform}", platformType.name().substring(0, 1).toUpperCase(Locale.ROOT) + platformType.name().substring(1).toLowerCase(Locale.ROOT));

            var resolvedElement = environment.getElementUtils().getTypeElement(resolvedClassName);
            if (resolvedElement == null && strict) {
                throw new UnsupportedOperationException("Can't find implementation of " + typeElement.getQualifiedName() + " for " + platformType);
            }
            if (resolvedElement != null && !environment.getTypeUtils().isAssignable(resolvedElement.asType(), typeElement.asType())) {
                throw new UnsupportedOperationException(resolvedElement.getQualifiedName() + " must be assignable to " + typeElement.getQualifiedName());
            }
            if (resolvedElement != null) {
                var resolvedElementService = resolvedElement.getAnnotation(Service.class);
                var container = new ServiceContainer(
                        environment.getTypeUtils(),
                        resolvedElement,
                        resolvedElement.getAnnotation(InternalEarlyInitialization.class) != null || typeElement.getAnnotation(InternalEarlyInitialization.class) != null,
                        (resolvedElementService != null && resolvedElementService.staticOnly()) || (LOMBOK_UTILITY_CLASS != null && typeElement.getAnnotation(LOMBOK_UTILITY_CLASS) != null),
                        resolvedElement.getAnnotation(InternalCoreService.class) != null || typeElement.getAnnotation(InternalCoreService.class) != null
                                || (typeElement.getKind() == ElementKind.CLASS && typeElement.getEnclosedElements().stream()
                                .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                                .allMatch(element -> element.getModifiers().contains(Modifier.PRIVATE))
                                && typeElement.getEnclosedElements().stream().noneMatch(element -> element.getKind() == ElementKind.METHOD && element.getAnnotation(ServiceInitializer.class) != null)),
                        false,
                        LOMBOK_UTILITY_CLASS != null && typeElement.getAnnotation(LOMBOK_UTILITY_CLASS) != null
                );
                if (resolvedElementService == null) {
                    environment.getMessager().printMessage(Diagnostic.Kind.WARNING, resolvedElement.getQualifiedName() + " should have @Service annotation (ignoring that because was resolved with @AbstractService)");
                }
                checkServiceDependencies(environment, resolvedElement, container);
                checkConstructorDependencies(environment, resolvedElement, container);
                checkEventManagerRequirement(environment, resolvedElement, container);
                checkAccessedPlugins(environment, resolvedElement, container);
                map.put(platformType, container);
            }
        });

        return map;
    }

    private static void checkEventManagerRequirement(ProcessingEnvironment environment, TypeElement typeElement, ServiceContainer container) {
        var eventManager = environment.getElementUtils().getTypeElement("org.screamingsandals.lib.event.EventManager");
        if (!container.getDependencies().contains(eventManager)) {
            var superClass = typeElement;
            do {
                var any = superClass.getEnclosedElements()
                        .stream()
                        .filter(element -> element.getKind() == ElementKind.METHOD
                                && element.getAnnotation(OnEvent.class) != null
                                && element.getModifiers().contains(Modifier.PUBLIC)
                        )
                        .findAny();
                if (any.isPresent()) {
                    container.getDependencies().add(eventManager);
                }
            } while ((superClass = (TypeElement) environment.getTypeUtils().asElement(superClass.getSuperclass())) != null);
        }
    }

    private static void checkServiceDependencies(ProcessingEnvironment environment, TypeElement typeElement, ServiceContainer container) {
        var superClass = typeElement;
        do {
            Arrays.stream(superClass
                    .getAnnotationsByType(ServiceDependencies.class))
                    .forEach(annotation -> {
                        getSafelyTypeElements(environment, annotation).forEach(typeElement1 -> {
                            if (container.getDependencies().stream().noneMatch(typeElement2 ->
                                    environment.getTypeUtils().isAssignable(typeElement2.asType(), typeElement1.asType()))
                            ) {
                                container.getDependencies().add(typeElement1);
                            }
                        });

                        getSafelyTypeElementsLoadAfter(environment, annotation).forEach(typeElement1 -> {
                            if (container.getLoadAfter().stream().noneMatch(typeElement2 ->
                                    environment.getTypeUtils().isAssignable(typeElement2.asType(), typeElement1.asType()))
                            ) {
                                container.getLoadAfter().add(typeElement1);
                            }
                        });

                        getSafelyTypeElementsInit(environment, annotation).forEach(typeElement1 -> {
                            if (container.getInit().stream().noneMatch(typeElement2 ->
                                    environment.getTypeUtils().isAssignable(typeElement2.asType(), typeElement1.asType()))
                            ) {
                                container.getInit().add(typeElement1);
                            }
                        });
                    });
        } while ((superClass = (TypeElement) environment.getTypeUtils().asElement(superClass.getSuperclass())) != null);
    }

    private static void checkAccessedPlugins(ProcessingEnvironment environment, TypeElement typeElement, ServiceContainer container) {
        var superClass = typeElement;
        do {
            Arrays.stream(superClass
                    .getAnnotationsByType(AccessPluginClasses.class))
                    .forEach(annotation -> {
                        for (var v : annotation.value()) {
                            container.getAccessedPlugins().add(v);
                        }
                    });
        } while ((superClass = (TypeElement) environment.getTypeUtils().asElement(superClass.getSuperclass())) != null);
    }

    private static void checkConstructorDependencies(ProcessingEnvironment environment, TypeElement typeElement, ServiceContainer container) {
        var initMethod = typeElement.getEnclosedElements()
                .stream()
                .filter(element -> (element.getKind() == ElementKind.METHOD || element.getKind() == ElementKind.CONSTRUCTOR) && element.getAnnotation(ServiceInitializer.class) != null)
                .findFirst();

        if (initMethod.isEmpty() && !container.isStaticOnly()) {
            initMethod = typeElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                    .findFirst();
        }

        if (initMethod.isPresent()) {
            var method = (ExecutableElement) initMethod.get();
            var arguments = method.getParameters();

            for (var argument : arguments) {
                var type = argument.asType();
                if (type.getKind() == TypeKind.DECLARED) {
                    var el = (TypeElement) environment.getTypeUtils().asElement(type);
                    if ((el.getAnnotation(Service.class) != null || el.getAnnotation(AbstractService.class) != null || el.getAnnotation(ProvidedService.class) != null) && !container.getDependencies().contains(el)) {
                        container.getDependencies().add(el);
                    }
                }
            }
        }
    }
}
