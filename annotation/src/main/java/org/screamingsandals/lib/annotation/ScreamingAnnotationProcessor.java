/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.annotation;

import lombok.SneakyThrows;
import org.screamingsandals.lib.annotation.generators.*;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@SupportedAnnotationTypes({
        "org.screamingsandals.lib.utils.annotations.Plugin"
})
@SupportedOptions({
        "lookForPluginAndSaveFullClassNameTo",
        "usePluginClassFrom"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ScreamingAnnotationProcessor extends AbstractProcessor {
    private TypeElement pluginContainer;

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        var usePluginClassFrom = processingEnv.getOptions().get("usePluginClassFrom");
        if (usePluginClassFrom == null) {
            var elements = roundEnv.getElementsAnnotatedWith(Plugin.class);
            if (!elements.isEmpty()) {
                if (pluginContainer != null || elements.size() > 1) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Plugin can be used only once");
                    throw new RuntimeException("@Plugin can be used only once");
                }
                var element = elements.iterator().next(); // why it's not list
                if (element.getKind() != ElementKind.CLASS || element.getModifiers().contains(Modifier.ABSTRACT)) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Plugin can be applied only too non-abstract class");
                    throw new RuntimeException("@Plugin can be applied only too non-abstract class");
                }
                pluginContainer = (TypeElement) element;
            }
        }

        if (roundEnv.processingOver()) {
            if (usePluginClassFrom != null) {
                var pluginClassName = Files.readString(Path.of(usePluginClassFrom));
                pluginContainer = processingEnv.getElementUtils().getTypeElement(pluginClassName);
                if (pluginContainer == null) {
                    throw new RuntimeException("Can't find plugin class " + pluginClassName);
                }
            }
            if (pluginContainer != null) {
                var lookForPluginAndSaveFullClassNameTo = processingEnv.getOptions().get("lookForPluginAndSaveFullClassNameTo");
                if (lookForPluginAndSaveFullClassNameTo != null) {
                    var path = Path.of(lookForPluginAndSaveFullClassNameTo);
                    Files.createDirectories(path);
                    Files.writeString(path, pluginContainer.getQualifiedName().toString(), StandardOpenOption.CREATE);
                    return true;
                }
                var platformInitiators = new HashMap<PlatformType, List<ServiceContainer>>();

                var platformManager = processingEnv.getElementUtils().getTypeElement("org.screamingsandals.lib.plugin.PluginManager");
                var platformManagers = MiscUtils.getAllSpecificPlatformImplementations(
                        processingEnv,
                        platformManager,
                        Arrays.asList(PlatformType.values().clone()),
                        false);
                platformManagers.forEach((platformType, typeElement) -> {
                    platformInitiators.put(platformType, new ArrayList<>() {
                        {
                            add(typeElement);
                        }
                    });
                    if (platformType.isServer()) {
                        var core = processingEnv.getElementUtils().getTypeElement("org.screamingsandals.lib.Core");
                        platformInitiators.get(platformType).add(
                                MiscUtils.getAllSpecificPlatformImplementations(
                                        processingEnv,
                                        core,
                                        List.of(platformType),
                                        true
                                ).get(platformType)
                        );
                    }
                    if (platformType.isProxy()) {
                        var proxy = processingEnv.getElementUtils().getTypeElement("org.screamingsandals.lib.proxy.ProxyCore");
                        platformInitiators.get(platformType).add(
                                MiscUtils.getAllSpecificPlatformImplementations(
                                        processingEnv,
                                        proxy,
                                        List.of(platformType),
                                        true
                                ).get(platformType)
                        );
                    }
                });

                var supportedPlatforms = List.copyOf(platformManagers.keySet());

                Arrays.stream(pluginContainer.getAnnotationsByType(Init.class)).forEach(init ->
                    processInitAnnotation(supportedPlatforms, platformInitiators, init)
                );

                if (platformInitiators.containsKey(PlatformType.BUKKIT)) {
                    new BukkitMainClassGenerator().generate(processingEnv, pluginContainer, platformInitiators.get(PlatformType.BUKKIT));
                }
                if (platformInitiators.containsKey(PlatformType.MINESTOM)) {
                    new MinestomMainClassGenerator().generate(processingEnv, pluginContainer, platformInitiators.get(PlatformType.MINESTOM));
                }
                if (platformInitiators.containsKey(PlatformType.SPONGE)) {
                    new SpongeMainClassGenerator().generate(processingEnv, pluginContainer, platformInitiators.get(PlatformType.SPONGE));
                }
                //if (platformInitiators.containsKey(PlatformType.FABRIC)) { }
                //if (platformInitiators.containsKey(PlatformType.FORGE)) { }
                if (platformInitiators.containsKey(PlatformType.BUNGEE)) {
                    new BungeeMainClassGenerator().generate(processingEnv, pluginContainer, platformInitiators.get(PlatformType.BUNGEE));
                }
                if (platformInitiators.containsKey(PlatformType.VELOCITY)) {
                    new VelocityMainClassGenerator().generate(processingEnv, pluginContainer, platformInitiators.get(PlatformType.VELOCITY));
                }
                //if (platformInitiators.containsKey(PlatformType.NUKKIT)) { }
            }
        }
        return true;
    }

    private void processInitAnnotation(List<PlatformType> supportedPlatforms, HashMap<PlatformType, List<ServiceContainer>> platformInitiators, Init init) {
        var platformList = new ArrayList<>(Arrays.asList(init.platforms()));
        if (platformList.isEmpty()) {
            platformList.addAll(supportedPlatforms);
        } else {
            platformList.retainAll(supportedPlatforms);
        }
        if (!platformList.isEmpty()) {
            MiscUtils.getSafelyTypeElements(processingEnv, init)
                    .stream()
                    .map(typeElement ->
                            MiscUtils.getAllSpecificPlatformImplementations(
                                    processingEnv,
                                    typeElement,
                                    platformList,
                                    true
                            )
                    )
                    .forEach(map ->
                            map.forEach((platformType, serviceContainer) -> {
                                if (!platformInitiators.get(platformType).contains(serviceContainer)) {
                                    platformInitiators.get(platformType).add(serviceContainer);
                                }
                            })
                    );

            for (var packageName : init.packages()) {
                var pkg = processingEnv.getElementUtils().getPackageElement(packageName);
                Arrays.stream(pkg.getAnnotationsByType(Init.class)).forEach(init2 ->
                        // why not supportedPlatforms? because we want to keep the effect of platforms field on the parent annotation
                        processInitAnnotation(platformList, platformInitiators, init2)
                );
            }
        }
    }
}