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
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.QualifiedNameable;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinestomMainClassGenerator extends StandardMainClassGenerator {
    @Override
    public void generate(@NotNull ProcessingEnvironment processingEnvironment, @NotNull QualifiedNameable pluginContainer, @NotNull List<ServiceContainer> autoInit) throws IOException {
        var newClassName = pluginContainer.getSimpleName() + "_MinestomImpl";

        var onLoadBuilder = preparePublicVoid("preInitialize");
        var onEnableBuilder = preparePublicVoid("initialize");
        var onDisableBuilder = preparePublicVoid("terminate");

        fillInOnLoadMethod(pluginContainer, autoInit, processingEnvironment, newClassName, onLoadBuilder);
        fillInOnEnableMethod(onEnableBuilder);
        fillInOnDisableMethod(onDisableBuilder);

        var minestomMainClass = prepareType(newClassName)
                .superclass(Classes.Minestom.EXTENSION)
                .addMethod(onLoadBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        String newClassPackage = getPackage(pluginContainer);

        JavaFile.builder(newClassPackage, minestomMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "extension.json").toUri()))
                .build();

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("entrypoint").set(newClassPackage + "." + newClassName);
        node.node("name").set(pluginAnnotation.id());
        node.node("version").set(pluginAnnotation.version());
        if (pluginAnnotation.authors().length > 0) {
            node.node("authors").set(pluginAnnotation.authors());
        }

        var dependencies = new ArrayList<String>();

        Arrays.stream(pluginContainer.getAnnotationsByType(PluginDependencies.class)).forEach(pluginDependencies -> {
            if (pluginDependencies.platform() == PlatformType.MINESTOM) {
                dependencies.addAll(Arrays.asList(pluginDependencies.dependencies()));
            }
        });

        if (!dependencies.isEmpty()) {
            node.node("dependencies").set(dependencies.stream().distinct().collect(Collectors.toList()));
        }

        loader.save(node);
    }

    @Override
    protected void prepareCustomDependencyInjectionTypes(@NotNull ServiceInitGenerator generator) {
        generator.add(Classes.Minestom.EXTENSION.canonicalName(), (statement, objects) ->
                statement.append(generator.getPlatformClassName()).append(".this")
        );
    }

    @Override
    protected void wrapPluginDescription(MethodSpec.@NotNull Builder builder) {
        builder.addStatement("$T $N = this.getDescription().getName()", String.class, "name")
                .addStatement("$T $N = $T.getPlugin($N)", Classes.SLib.PLUGIN, "description", Classes.SLib.PLUGINS_SERVICE, "name"); // TODO: direct class initialization
    }

    @Override
    protected void wrapLoggers(MethodSpec.@NotNull Builder builder, boolean requiredScreamingLogger, boolean requiredSlf4jLogger) {
        if (requiredScreamingLogger || requiredSlf4jLogger) {
            builder.addStatement("$T $N = this.getLogger()", Classes.Slf4j.LOGGER, "slf4jLogger");
        }
        if (requiredScreamingLogger) {
            builder.addStatement("$T $N = new $T($N)", Classes.SLib.SLF4J_LOGGER_WRAPPER, "screamingLogger", Classes.SLib.SLF4J_LOGGER_WRAPPER, "slf4jLogger");
        }
    }

    @Override
    protected @NotNull PlatformType getPlatform() {
        return PlatformType.MINESTOM;
    }
}
