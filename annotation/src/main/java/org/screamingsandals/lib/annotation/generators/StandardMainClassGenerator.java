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
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.annotation.constants.Classes;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import java.util.List;

public abstract class StandardMainClassGenerator extends MainClassGenerator {
    protected void fillInOnLoadMethod(@NotNull QualifiedNameable pluginContainer, @NotNull List<@NotNull ServiceContainer> autoInit, @NotNull ProcessingEnvironment processingEnvironment, @NotNull String mainClassName, MethodSpec.@NotNull Builder builder) {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, getPlatform());

        var onLoadCodeBlock = CodeBlock.builder()
                .addStatement("this.$N = new $T()", "pluginControllable", Classes.SLib.CONTROLLABLE_IMPL);
        var serviceInitGenerator = new ServiceInitGenerator(mainClassName, onLoadCodeBlock, processingEnvironment.getTypeUtils(), processingEnvironment.getElementUtils());
        prepareCustomDependencyInjectionTypes(serviceInitGenerator);

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        if (pluginContainer instanceof TypeElement) {
            serviceInitGenerator.process(ServiceContainer.createPluginService(processingEnvironment.getTypeUtils(), (TypeElement) pluginContainer));
        }

        sortedPair.getSecond().forEach(serviceInitGenerator::process);

        serviceInitGenerator.processDelayedControllables();

        if (serviceInitGenerator.isRequiredDescription()) {
            wrapPluginDescription(builder);
        }

        if (serviceInitGenerator.isRequiredScreamingLogger() || serviceInitGenerator.isRequiredSlf4jLogger()) {
            wrapLoggers(builder, serviceInitGenerator.isRequiredScreamingLogger(), serviceInitGenerator.isRequiredSlf4jLogger());
        }

        builder
                .addCode(onLoadCodeBlock.build())
                .addStatement("this.$N.pluginLoad()", "pluginControllable");
    }

    protected void fillInOnEnableMethod(MethodSpec.@NotNull Builder builder) {
        builder
                .addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.postEnable()", "pluginControllable");
    }

    protected void fillInOnDisableMethod(MethodSpec.@NotNull Builder builder) {
        builder
                .addStatement("this.$N.preDisable()", "pluginControllable")
                .addStatement("this.$N.disable()", "pluginControllable");
    }

    protected TypeSpec.@NotNull Builder prepareType(@NotNull String className) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                        .builder(Classes.SLib.CONTROLLABLE_IMPL, "pluginControllable", Modifier.PRIVATE)
                        .build()
                );
    }

    protected @NotNull String getPackage(@NotNull QualifiedNameable pluginContainer) {
        if (pluginContainer instanceof TypeElement) { // class
            return ((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString();
        } else { // package
            return pluginContainer.getQualifiedName().toString();
        }
    }

    protected abstract void prepareCustomDependencyInjectionTypes(@NotNull ServiceInitGenerator generator);

    protected abstract void wrapPluginDescription(MethodSpec.@NotNull Builder builder);

    protected abstract void wrapLoggers(MethodSpec.@NotNull Builder builder, boolean requiredScreamingLogger, boolean requiredSlf4jLogger);

    protected abstract @NotNull PlatformType getPlatform();
}
