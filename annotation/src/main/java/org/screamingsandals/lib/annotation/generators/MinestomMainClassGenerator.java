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

public class MinestomMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, QualifiedNameable pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.MINESTOM);

        var pluginManagerClass = Classes.SLib.PLUGINS_SERVICE;
        var pluginDescriptionClass = Classes.SLib.PLUGIN;
        var screamingLoggerClass = Classes.SLib.SLF4J_LOGGER_WRAPPER;
        var loggerClass = Classes.Slf4j.LOGGER;

        var onLoadBuilder = preparePublicVoid("preInitialize")
                .addStatement("this.$N = new $T()", "pluginControllable", Classes.SLib.CONTROLLABLE_IMPL);

        var newClassName = pluginContainer.getSimpleName() + "_MinestomImpl";

        var serviceInitGenerator = new ServiceInitGenerator(newClassName, onLoadBuilder, processingEnvironment.getTypeUtils(), processingEnvironment.getElementUtils())
                .add(Classes.Minestom.EXTENSION.canonicalName(), (statement, objects) ->
                        statement.append(newClassName).append(".this")
                );

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        onLoadBuilder.addStatement("$T $N = this.getDescription().getName()", String.class, "name")
                .addStatement("$T $N = $T.getPlugin($N)", pluginDescriptionClass, "description", pluginManagerClass, "name")
                .addStatement("$T $N = this.getLogger()", loggerClass, "slf4jLogger")
                .addStatement("$T $N = new $T($N)", screamingLoggerClass, "screamingLogger", screamingLoggerClass, "slf4jLogger");

        if (pluginContainer instanceof TypeElement) {
            serviceInitGenerator.process(ServiceContainer.createPluginService(processingEnvironment.getTypeUtils(), (TypeElement) pluginContainer));
        }


        var onEnableBuilder = preparePublicVoid("initialize");
        var onDisableBuilder = preparePublicVoid("terminate");

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

        var minestomMainClass = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Classes.Minestom.EXTENSION)
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

        JavaFile.builder(newPackageName, minestomMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "extension.json").toUri()))
                .build();

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("entrypoint").set(newPackageName + "." + newClassName);
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
}
