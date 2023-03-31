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
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, QualifiedNameable pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.BUNGEE);

        var pluginManagerClass = Classes.SLib.PLUGINS_SERVICE;
        var pluginDescriptionClass = Classes.SLib.PLUGIN;
        var screamingLoggerClass = Classes.SLib.LOGGER_WRAPPER;
        var julScreamingLoggerClass = Classes.SLib.JUL_LOGGER_WRAPPER;
        var slf4jScreamingLoggerClass = Classes.SLib.SLF4J_LOGGER_WRAPPER;
        var dualScreamingLoggerClass = Classes.SLib.DUAL_LOGGER_WRAPPER;
        var reflectClass = Classes.SLib.REFLECT;

        var onLoadBuilder = preparePublicVoid("onLoad")
                .addStatement("this.$N = new $T()", "pluginControllable", Classes.SLib.CONTROLLABLE_IMPL);

        var newClassName = pluginContainer.getSimpleName() + "_BungeeImpl";

        var serviceInitGenerator = new ServiceInitGenerator(newClassName, onLoadBuilder, processingEnvironment.getTypeUtils(), processingEnvironment.getElementUtils())
                .add(Classes.Bungee.PLUGIN.canonicalName(), (statement, objects) ->
                        statement.append(newClassName).append(".this")
                );

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        onLoadBuilder
                .addStatement("$T $N = new $T(this)", pluginDescriptionClass, "description", Classes.SLib.BUNGEE_PLUGIN)
                .addStatement("$T $N = null", Object.class, "slf4jLogger")
                .addStatement("$T $N = new $T(this.getLogger())", screamingLoggerClass, "screamingLogger", julScreamingLoggerClass)
                .beginControlFlow("if ($T.hasMethod(this, $S))", reflectClass, "getSLF4JLogger")
                    .addStatement("$N = this.getSLF4JLogger()", "slf4jLogger")
                    .addStatement("$N = new $T(new $T(this.getSLF4JLogger()), $N)", "screamingLogger", dualScreamingLoggerClass, slf4jScreamingLoggerClass, "screamingLogger")
                .endControlFlow();

        if (pluginContainer instanceof TypeElement) {
            serviceInitGenerator.process(ServiceContainer.createPluginService(processingEnvironment.getTypeUtils(), (TypeElement) pluginContainer));
        }

        var onEnableBuilder = preparePublicVoid("onEnable");
        var onDisableBuilder = preparePublicVoid("onDisable");

        sortedPair.getSecond().forEach(serviceInitGenerator::process);

        serviceInitGenerator.processDelayedControllables();

        onLoadBuilder
                .addStatement("this.$N.pluginLoad()", "pluginControllable");

        onEnableBuilder
                .addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.postEnable()", "pluginControllable");

        onDisableBuilder
                .addStatement("this.$N.preDisable()", "pluginControllable")
                .addStatement("this.$N.disable()", "pluginControllable");

        var bungeeMainClass = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Classes.Bungee.PLUGIN)
                .addField(FieldSpec
                        .builder(Classes.SLib.CONTROLLABLE_IMPL, "pluginControllable", Modifier.PRIVATE)
                        .build())
                .addMethod(onLoadBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        String newPackageName;
        if (pluginContainer instanceof TypeElement) { // class
            newPackageName = ((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString();
        } else { // package
            newPackageName = pluginContainer.getQualifiedName().toString();
        }

        JavaFile.builder(newPackageName, bungeeMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = YamlConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "bungee.yml").toUri()))
                .build();


        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("main").set(newPackageName + "." + newClassName);
        node.node("name").set(pluginAnnotation.id());
        node.node("version").set(pluginAnnotation.version());
        if (!pluginAnnotation.description().isBlank()) {
            node.node("description").set(pluginAnnotation.description());
        }
        if (pluginAnnotation.authors().length > 0) {
            node.node("author").set(String.join(", ", pluginAnnotation.authors()));
        } else {
            node.node("author").set("Unspecified");
        }

        var depends = new ArrayList<String>();
        var softDepends = new ArrayList<String>();

        Arrays.stream(pluginContainer.getAnnotationsByType(PluginDependencies.class)).forEach(pluginDependencies -> {
            if (pluginDependencies.platform() == PlatformType.BUNGEE) {
                depends.addAll(Arrays.asList(pluginDependencies.dependencies()));
                softDepends.addAll(Arrays.asList(pluginDependencies.softDependencies()));
            }
        });

        if (!depends.isEmpty()) {
            node.node("depends").set(depends.stream().distinct().collect(Collectors.toList()));
        }

        if (!softDepends.isEmpty()) {
            node.node("softDepends").set(softDepends.stream().distinct().collect(Collectors.toList()));
        }

        loader.save(node);
    }
}
