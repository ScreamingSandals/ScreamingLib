package org.screamingsandals.lib.annotation;

import lombok.SneakyThrows;
import org.screamingsandals.lib.annotation.generators.*;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes({
        "org.screamingsandals.lib.utils.annotations.Plugin"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ScreamingAnnotationProcessor extends AbstractProcessor {
    private TypeElement pluginContainer;

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
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

        if (roundEnv.processingOver()) {
            if (pluginContainer != null) {
                var platformInitiators = new HashMap<PlatformType, List<TypeElement>>();

                var platformManager = processingEnv.getElementUtils().getTypeElement("org.screamingsandals.lib.plugin.PluginManager");
                var platformManagers = MiscUtils.getAllSpecificPlatformImplementations(
                        processingEnv,
                        platformManager,
                        Arrays.asList(PlatformType.values().clone()),
                        false);
                platformManagers.forEach((platformType, typeElement) ->
                        platformInitiators.put(platformType, new ArrayList<>() {
                            {
                                add(typeElement);
                            }
                        })
                );

                var supportedPlatforms = List.copyOf(platformManagers.keySet());

                Arrays.stream(pluginContainer.getAnnotationsByType(Init.class)).forEach(init -> {
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
                                        map.forEach((platformType, typeElement) -> {
                                            if (!platformInitiators.get(platformType).contains(typeElement)) {
                                                platformInitiators.get(platformType).add(typeElement);
                                            }
                                        })
                                );
                    }
                });

                System.out.println("PROCESSING PLUGIN CONTAINER " + pluginContainer);
                System.out.println("Initiators " + platformInitiators);
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
}