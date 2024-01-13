/*
 * Copyright 2024 ScreamingSandals
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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.annotation.constants.Classes;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VelocityMainClassGenerator extends StandardMainClassGenerator {
    @Override
    public void generate(@NotNull ProcessingEnvironment processingEnvironment, @NotNull QualifiedNameable pluginContainer, @NotNull List<ServiceContainer> autoInit) throws IOException {
        var constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Classes.Guice.INJECT)
                .addParameter(ParameterSpec.builder(Classes.Velocity.PROXY_SERVER, "proxyServer").build())
                .addParameter(ParameterSpec.builder(Classes.Slf4j.LOGGER, "slf4jLogger").build())
                .addParameter(ParameterSpec.builder(Classes.Guice.INJECTOR, "guice").build())
                .addParameter(ParameterSpec.builder(Classes.Velocity.PLUGIN_CONTAINER, "velocityContainer").build())
                .addStatement("this.$N = $N", "guiceInjector", "guice")
                .addStatement("this.$N = $N", "slf4jLogger", "slf4jLogger")
                .addStatement("this.$N = $N", "velocityContainer", "velocityContainer");

        var newClassName = pluginContainer.getSimpleName() + "_VelocityImpl";

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var onEnableBuilder = preparePublicVoid("onEnable")
                .addAnnotation(Classes.Velocity.SUBSCRIBE)
                .addParameter(ParameterSpec.builder(Classes.Velocity.PROXY_INIT_EVENT, "event").build());

        var onDisableBuilder = preparePublicVoid("onDisable")
                .addAnnotation(Classes.Velocity.SUBSCRIBE)
                .addParameter(ParameterSpec.builder(Classes.Velocity.PROXY_SHUTDOWN_EVENT, "event").build());

        fillInOnLoadMethod(pluginContainer, autoInit, processingEnvironment, newClassName, onEnableBuilder); // there's no load method in Velocity, process earlier in on enable
        fillInOnEnableMethod(onEnableBuilder);
        fillInOnDisableMethod(onDisableBuilder);

        var velocityMainClass = prepareType(newClassName)
                .addField(FieldSpec
                        .builder(Classes.Slf4j.LOGGER, "slf4jLogger", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(Classes.Velocity.PLUGIN_CONTAINER, "velocityContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(Classes.Guice.INJECTOR, "guiceInjector", Modifier.PRIVATE)
                        .build())
                .addMethod(constructorBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        String newClassPackage = getPackage(pluginContainer);

        JavaFile.builder(newClassPackage, velocityMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "velocity-plugin.json").toUri()))
                .build();

        var node = loader.createNode();
        node.node("main").set(newClassPackage + "." + newClassName);
        node.node("id").set(pluginAnnotation.id().toLowerCase(Locale.ROOT));
        node.node("name").set(pluginAnnotation.name());
        node.node("version").set(pluginAnnotation.version());
        if (!pluginAnnotation.description().isBlank()) {
            node.node("description").set(pluginAnnotation.description());
        }
        if (pluginAnnotation.authors().length > 0) {
            node.node("authors").set(pluginAnnotation.authors());
        }

        var dependencies = new ArrayList<String>();

        Arrays.stream(pluginContainer.getAnnotationsByType(PluginDependencies.class)).forEach(pluginDependencies -> {
            if (pluginDependencies.platform() == PlatformType.VELOCITY) {
                dependencies.addAll(Arrays.asList(pluginDependencies.dependencies()));
            }
        });

        loader.save(node);
    }

    @Override
    protected void prepareCustomDependencyInjectionTypes(@NotNull ServiceInitGenerator generator) {
        generator.add(Classes.Velocity.PROXY_SERVER.canonicalName(), (statement, objects) -> {
                    statement.append("$N");
                    objects.add("proxyServer");
                })
                .add(Classes.Velocity.PLUGIN_MANAGER.canonicalName(), (statement, objects) -> {
                    statement.append("$N.getPluginManager()");
                    objects.add("proxyServer");
                });
    }

    @Override
    protected void wrapPluginDescription(MethodSpec.@NotNull Builder builder) {
        builder.addStatement("$T $N = new $T($N)", Classes.SLib.PLUGIN, "description", Classes.SLib.VELOCITY_PLUGIN, "velocityContainer");
    }

    @Override
    protected void wrapLoggers(MethodSpec.@NotNull Builder builder, boolean requiredScreamingLogger, boolean requiredSlf4jLogger) {
        if (requiredScreamingLogger) {
            builder.addStatement("this.$N = new $T($N)", "screamingLogger", Classes.SLib.SLF4J_LOGGER_WRAPPER, "slf4jLogger");
        }
    }

    @Override
    protected @NotNull PlatformType getPlatform() {
        return PlatformType.VELOCITY;
    }
}
