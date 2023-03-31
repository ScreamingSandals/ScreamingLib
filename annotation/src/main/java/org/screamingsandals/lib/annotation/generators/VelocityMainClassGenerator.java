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

import com.squareup.javapoet.*;
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

public class VelocityMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, QualifiedNameable pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.VELOCITY);

        var pluginDescriptionClass = Classes.SLib.PLUGIN;
        var screamingLoggerClass = Classes.SLib.SLF4J_LOGGER_WRAPPER;
        var loggerClass = Classes.Slf4j.LOGGER;

        var velocityProxyServerClass = Classes.Velocity.PROXY_SERVER;
        var guiceInjectorClass = Classes.Guice.INJECTOR;

        var constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Classes.Guice.INJECT)
                .addParameter(ParameterSpec.builder(velocityProxyServerClass, "proxyServer").build())
                .addParameter(ParameterSpec.builder(loggerClass, "slf4jLogger").build())
                .addParameter(ParameterSpec.builder(guiceInjectorClass, "guice").build())
                .addParameter(ParameterSpec.builder(Classes.Velocity.PLUGIN_CONTAINER, "velocityContainer").build())
                .addStatement("this.$N = new $T()", "pluginControllable", Classes.SLib.CONTROLLABLE_IMPL)
                .addStatement("this.$N = $N", "guiceInjector", "guice")
                .addStatement("this.$N = new $T($N)", "screamingLogger", screamingLoggerClass, "slf4jLogger")
                .addStatement("this.$N = $N", "velocityContainer", "velocityContainer");

        var newClassName = pluginContainer.getSimpleName() + "_VelocityImpl";

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var onEnableBuilder = preparePublicVoid("onEnable")
                .addAnnotation(Classes.Velocity.SUBSCRIBE)
                .addParameter(ParameterSpec.builder(Classes.Velocity.PROXY_INIT_EVENT, "event").build());

        var onDisableBuilder = preparePublicVoid("onDisable")
                .addAnnotation(Classes.Velocity.SUBSCRIBE)
                .addParameter(ParameterSpec.builder(Classes.Velocity.PROXY_SHUTDOWN_EVENT, "event").build());

        var serviceInitGenerator = new ServiceInitGenerator(newClassName, onEnableBuilder, processingEnvironment.getTypeUtils(), processingEnvironment.getElementUtils())
                .add(velocityProxyServerClass.canonicalName(), (statement, objects) -> {
                    statement.append("$N");
                    objects.add("proxyServer");
                })
                .add(Classes.Velocity.PLUGIN_MANAGER.canonicalName(), (statement, objects) -> {
                    statement.append("$N.getPluginManager()");
                    objects.add("proxyServer");
                });

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        onEnableBuilder
                .addStatement("$T $N = new $T($N)", pluginDescriptionClass, "description", Classes.SLib.VELOCITY_PLUGIN, "velocityContainer");

        if (pluginContainer instanceof TypeElement) {
            serviceInitGenerator.process(ServiceContainer.createPluginService(processingEnvironment.getTypeUtils(), (TypeElement) pluginContainer));
        }

        sortedPair.getSecond().forEach(serviceInitGenerator::process);

        serviceInitGenerator.processDelayedControllables();

        //load the container first
        onEnableBuilder
                .addStatement("this.$N.pluginLoad()", "pluginControllable");

        //then enable
        onEnableBuilder.addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.postEnable()", "pluginControllable");

        onDisableBuilder
                .addStatement("this.$N.preDisable()", "pluginControllable")
                .addStatement("this.$N.disable()", "pluginControllable");

        var velocityMainClass = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec
                        .builder(Classes.SLib.CONTROLLABLE_IMPL, "pluginControllable",
                                Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(screamingLoggerClass, "screamingLogger", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(Classes.Velocity.PLUGIN_CONTAINER, "velocityContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(guiceInjectorClass, "guiceInjector", Modifier.PRIVATE)
                        .build())
                .addMethod(constructorBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        String newPackageName;
        if (pluginContainer instanceof TypeElement) { // class
            newPackageName = ((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString();
        } else { // package
            newPackageName = pluginContainer.getQualifiedName().toString();
        }

        JavaFile.builder(newPackageName, velocityMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "velocity-plugin.json").toUri()))
                .build();

        var node = loader.createNode();
        node.node("main").set(newPackageName + "." + newClassName);
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
}
