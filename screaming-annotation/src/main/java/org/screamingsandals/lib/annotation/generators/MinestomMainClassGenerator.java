package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.*;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MinestomMainClassGenerator implements MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sorted = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.MINESTOM);

        var pluginManager = new AtomicReference<ServiceContainer>();

        sorted.removeIf(serviceContainer -> {
            if (pluginManager.get() == null && serviceContainer.getAbstractService().getQualifiedName().toString().equals("org.screamingsandals.lib.plugin.PluginManager")) {
                pluginManager.set(serviceContainer);
                return true;
            }
            return false;
        });

        var pluginManagerClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginManager");
        var pluginDescriptionClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginDescription");
        var pluginKeyClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginKey");
        var loggerClass = ClassName.get("org.slf4j", "Logger");

        var onLoadBuilder = MethodSpec.methodBuilder("preInitialize")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("this.$N = new $T()", "pluginControllable", ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"));


        var serviceInitGenerator = ServiceInitGenerator
                .builder(onLoadBuilder)
                .add("net.minestom.server.extensions.Extension", (statement, objects) ->
                        statement.append("this")
                );

        serviceInitGenerator.process(pluginManager.get());

        onLoadBuilder.addStatement("$T $N = this.getDescription().getName()", String.class, "name")
                .addStatement("$T $N = $T.createKey($N).orElseThrow()", pluginKeyClass, "key", pluginManagerClass, "name")
                .addStatement("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key")
                .addStatement("this.$N = new $T()", "pluginContainer", pluginContainer)
                .addStatement("$T $N = this.getLogger()", loggerClass, "slf4jLogger")
                .addStatement("this.$N.init($N, $N)", "pluginContainer", "description", "slf4jLogger");

        var onEnableBuilder = MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        var onDisableBuilder = MethodSpec.methodBuilder("terminate")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        sorted.forEach(serviceInitGenerator::process);

        onLoadBuilder
                .addStatement("this.$N.load()", "pluginContainer");

        onEnableBuilder
                .beginControlFlow("if (this.$N == null)", "pluginContainer")
                .addStatement("throw new $T($S)", UnsupportedOperationException.class, "Plugin must be loaded before enabling!")
                .endControlFlow()
                .addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.enable()", "pluginContainer")
                .addStatement("this.$N.postEnable()", "pluginControllable");

        onDisableBuilder
                .addStatement("this.$N.preDisable()", "pluginControllable")
                .addStatement("this.$N.disable()", "pluginContainer")
                .addStatement("this.$N.disable()", "pluginControllable");

        var minestomMainClass = TypeSpec.classBuilder(pluginContainer.getSimpleName() + "_MinestomImpl")
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.get("net.minestom.server.extensions", "Extension"))
                .addField(FieldSpec
                        .builder(TypeName.get(pluginContainer.asType()), "pluginContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"), "pluginControllable",
                                Modifier.PRIVATE)
                        .build())
                .addMethod(onLoadBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        JavaFile.builder(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString(), minestomMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "extension.json").toUri()))
                .build();

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("entrypoint").set(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString() + "." + pluginContainer.getSimpleName() + "_MinestomImpl");
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
