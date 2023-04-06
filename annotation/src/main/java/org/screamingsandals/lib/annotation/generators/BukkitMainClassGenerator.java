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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.annotation.constants.Classes;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitMainClassGenerator extends StandardMainClassGenerator {
    @Override
    public void generate(@NotNull ProcessingEnvironment processingEnvironment, @NotNull QualifiedNameable pluginContainer, @NotNull List<@NotNull ServiceContainer> autoInit) throws IOException {
        var newClassName = pluginContainer.getSimpleName() + "_BukkitImpl";

        var onLoadBuilder = preparePublicVoid("onLoad");
        var onEnableBuilder = preparePublicVoid("onEnable");
        var onDisableBuilder = preparePublicVoid("onDisable");

        fillInOnLoadMethod(pluginContainer, autoInit, processingEnvironment, newClassName, onLoadBuilder);
        fillInOnEnableMethod(onEnableBuilder);
        fillInOnDisableMethod(onDisableBuilder);

        var bukkitMainClass = prepareType(newClassName)
                .superclass(Classes.Bukkit.JAVA_PLUGIN)
                .addMethod(onLoadBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        String newClassPackage = getPackage(pluginContainer);

        JavaFile.builder(newClassPackage, bukkitMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = YamlConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml").toUri()))
                .build();

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("main").set(newClassPackage + "." + newClassName);
        node.node("name").set(pluginAnnotation.id());
        node.node("version").set(pluginAnnotation.version());
        node.node("api-version").set("1.13");
        node.node("load").set(pluginAnnotation.loadTime().name());
        if (!pluginAnnotation.name().isBlank()) {
            node.node("prefix").set(pluginAnnotation.name());
        }
        if (!pluginAnnotation.description().isBlank()) {
            node.node("description").set(pluginAnnotation.description());
        }
        if (pluginAnnotation.authors().length > 0) {
            node.node("authors").set(Arrays.asList(pluginAnnotation.authors()));
        }

        var depend = new ArrayList<String>();
        var softdepend = new ArrayList<String>();
        var loadbefore = new ArrayList<String>();

        Arrays.stream(pluginContainer.getAnnotationsByType(PluginDependencies.class)).forEach(pluginDependencies -> {
            if (pluginDependencies.platform() == PlatformType.BUKKIT) {
                depend.addAll(Arrays.asList(pluginDependencies.dependencies()));
                softdepend.addAll(Arrays.asList(pluginDependencies.softDependencies()));
                loadbefore.addAll(Arrays.asList(pluginDependencies.loadBefore()));
            }
        });

        if (!depend.isEmpty()) {
            node.node("depend").set(depend.stream().distinct().collect(Collectors.toList()));
        }

        if (!softdepend.isEmpty()) {
            node.node("softdepend").set(softdepend.stream().distinct().collect(Collectors.toList()));
        }

        if (!loadbefore.isEmpty()) {
            node.node("loadbefore").set(loadbefore.stream().distinct().collect(Collectors.toList()));
        }

        loader.save(node);

    }

    @Override
    public void prepareCustomDependencyInjectionTypes(@NotNull ServiceInitGenerator generator) {
        generator.add(List.of(Classes.Bukkit.JAVA_PLUGIN.canonicalName(), Classes.Bukkit.PLUGIN_BASE.canonicalName(), Classes.Bukkit.PLUGIN.canonicalName()), (statement, objects) ->
            statement.append(generator.getPlatformClassName()).append(".this")
        );
    }

    @Override
    public void wrapPluginDescription(MethodSpec.@NotNull Builder builder) {
        builder
                .addStatement("$T $N = new $T(this)", Classes.SLib.PLUGIN, "description", Classes.SLib.BUKKIT_PLUGIN);
    }

    @Override
    public void wrapLoggers(MethodSpec.@NotNull Builder builder, boolean requiredScreamingLogger, boolean requiredSlf4jLogger) {
        if (requiredScreamingLogger) { // works for requiredScreamingLogger && requiredSlf4jLogger as well
            builder
                    .addStatement("$T $N = null", Object.class, "slf4jLogger")
                    .addStatement("$T $N = new $T(this.getLogger())", Classes.SLib.LOGGER_WRAPPER, "screamingLogger", Classes.SLib.JUL_LOGGER_WRAPPER)
                    .beginControlFlow("if ($T.hasMethod(this, $S))", Classes.SLib.REFLECT, "getSLF4JLogger")
                        .addStatement("$N = this.getSLF4JLogger()", "slf4jLogger")
                        .addStatement("$N = new $T(new $T(this.getSLF4JLogger()), $N)", "screamingLogger", Classes.SLib.DUAL_LOGGER_WRAPPER, Classes.SLib.SLF4J_LOGGER_WRAPPER, "screamingLogger")
                    .endControlFlow();
        } else if (requiredSlf4jLogger) {
            builder
                    .addStatement("$T $N = null", Object.class, "slf4jLogger")
                    .beginControlFlow("if ($T.hasMethod(this, $S))", Classes.SLib.REFLECT, "getSLF4JLogger")
                        .addStatement("$N = this.getSLF4JLogger()", "slf4jLogger")
                    .endControlFlow();

        }
    }

    @Override
    public @NotNull PlatformType getPlatform() {
        return PlatformType.BUKKIT;
    }
}
