package org.screamingsandals.lib.annotation.utils;

import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MiscUtils {
    public static List<TypeElement> getSafelyTypeElements(ProcessingEnvironment environment, Service annotation) {
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
    public static List<TypeElement> getSafelyTypeElementsLoadAfter(ProcessingEnvironment environment, Service annotation) {
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

    @SuppressWarnings("unchecked")
    public static Map<PlatformType, ServiceContainer> getAllSpecificPlatformImplementations(ProcessingEnvironment environment, TypeElement typeElement, List<PlatformType> platformTypes, boolean strict) {
        var mappingAnnotation = typeElement.getAnnotation(AbstractService.class);
        if (mappingAnnotation == null) {
            var service = typeElement.getAnnotation(Service.class);
            if (service != null) {
                var container = new ServiceContainer(typeElement, null);
                container.getDependencies().addAll(getSafelyTypeElements(environment, service));
                container.getLoadAfter().addAll(getSafelyTypeElementsLoadAfter(environment, service));

                return platformTypes
                        .stream()
                        .collect(Collectors.toMap(s -> s, o -> container));
            }
            throw new UnsupportedOperationException("Type " + typeElement.getQualifiedName() + " doesn't have @AbstractMapping annotation");
        }

        var pattern = Pattern.compile(mappingAnnotation.pattern());
        var matcher = pattern.matcher(typeElement.getQualifiedName().toString());

        if (!matcher.matches()) {
            throw new UnsupportedOperationException("Pattern in @AbstractMapping annotation of " + typeElement.getQualifiedName() + " is invalid");
        }

        var namedGroups = (Map<String, Integer>) Reflect.fastInvoke(pattern, "namedGroups");

        var resolvedGroups = namedGroups.keySet()
                .stream()
                .collect(Collectors.toMap(s -> s, matcher::group));

        var map = new HashMap<PlatformType, ServiceContainer>();

        var rule = mappingAnnotation.replaceRule();
        for (var resolved : resolvedGroups.entrySet()) {
            rule = rule.replaceAll("\\{" + resolved.getKey() + "}", resolved.getValue());
        }
        final var finalRule = rule;

        platformTypes.forEach(platformType -> {
            var resolvedClassName = finalRule
                    .replaceAll("\\{platform}", platformType.name().toLowerCase())
                    .replaceAll("\\{Platform}", platformType.name().substring(0, 1).toUpperCase() + platformType.name().substring(1).toLowerCase());

            var resolvedElement = environment.getElementUtils().getTypeElement(resolvedClassName);
            if (resolvedElement == null && strict) {
                throw new UnsupportedOperationException("Can't find implementation of " + typeElement.getQualifiedName() + " for " + platformType);
            }
            if (resolvedElement != null) {
                var container = new ServiceContainer(resolvedElement, typeElement);
                var resolvedElementService = resolvedElement.getAnnotation(Service.class);
                if (resolvedElementService != null) {
                    container.getDependencies().addAll(getSafelyTypeElements(environment, resolvedElement.getAnnotation(Service.class)));
                    container.getLoadAfter().addAll(getSafelyTypeElementsLoadAfter(environment, resolvedElement.getAnnotation(Service.class)));
                } else {
                    environment.getMessager().printMessage(Diagnostic.Kind.WARNING, resolvedElement.getQualifiedName() + " should have @Service annotation (ignoring that because was resolved with @AbstractService)");
                }
                map.put(platformType, container);
            }
        });

        return map;
    }
}
