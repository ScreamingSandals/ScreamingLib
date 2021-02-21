package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.*;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
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
import java.util.stream.Collectors;

public class BungeeMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.BUNGEE);

        var pluginManagerClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginManager");
        var pluginDescriptionClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginDescription");
        var pluginKeyClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginKey");
        var screamingLoggerClass = ClassName.get("org.screamingsandals.lib.plugin.logger", "LoggerWrapper");
        var julScreamingLoggerClass = ClassName.get("org.screamingsandals.lib.plugin.logger", "JULLoggerWrapper");
        var slf4jScreamingLoggerClass = ClassName.get("org.screamingsandals.lib.plugin.logger", "Slf4jLoggerWrapper");
        var dualScreamingLoggerClass = ClassName.get("org.screamingsandals.lib.plugin.logger", "DualLoggerWrapper");
        var reflectClass = ClassName.get("org.screamingsandals.lib.utils.reflect", "Reflect");

        var onLoadBuilder = preparePublicVoid("onLoad")
                .addStatement("this.$N = new $T()", "pluginControllable", ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"));


        var serviceInitGenerator = ServiceInitGenerator
                .builder(onLoadBuilder, processingEnvironment.getTypeUtils())
                .add("net.md_5.bungee.api.plugin.Plugin", (statement, objects) ->
                        statement.append("this")
                );

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        onLoadBuilder.addStatement("$T $N = this.getDescription().getName()", String.class, "name")
                .addStatement("$T $N = $T.createKey($N).orElseThrow()", pluginKeyClass, "key", pluginManagerClass, "name")
                .addStatement("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key")
                .addStatement("this.$N = new $T()", "pluginContainer", pluginContainer)
                .addStatement("$T $N = null", Object.class, "slf4jLogger")
                .addStatement("$T $N = new $T(this.getLogger())", screamingLoggerClass, "screamingLogger", julScreamingLoggerClass)
                .beginControlFlow("if ($T.getMethod(this, $S) != null)", reflectClass, "getSLF4JLogger")
                    .addStatement("$N = this.getSLF4JLogger()", "slf4jLogger")
                    .addStatement("$N = new $T(new $T(this.getSLF4JLogger()), $N)", "screamingLogger", dualScreamingLoggerClass, slf4jScreamingLoggerClass, "screamingLogger")
                .endControlFlow()
                .addStatement("this.$N.init($N, $N)", "pluginContainer", "description", "screamingLogger");

        var onEnableBuilder = preparePublicVoid("onEnable");
        var onDisableBuilder = preparePublicVoid("onDisable");

        sortedPair.getSecond().forEach(serviceInitGenerator::process);

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

        var bungeeMainClass = TypeSpec.classBuilder(pluginContainer.getSimpleName() + "_BungeeImpl")
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.get("net.md_5.bungee.api.plugin", "Plugin"))
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

        JavaFile.builder(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString(), bungeeMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = YamlConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "bungee.yml").toUri()))
                .build();


        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("main").set(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString() + "." + pluginContainer.getSimpleName() + "_BungeeImpl");
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
