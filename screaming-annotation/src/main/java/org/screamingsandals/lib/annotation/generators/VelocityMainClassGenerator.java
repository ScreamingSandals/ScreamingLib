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

public class VelocityMainClassGenerator implements MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sorted = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.VELOCITY);

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

        var velocityProxyServerClass = ClassName.get("com.velocitypowered.api.proxy", "ProxyServer");
        var velocityPluginDescriptionClass = ClassName.get("com.velocitypowered.api.plugin", "PluginDescription");

        var constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("com.google.inject", "Inject"))
                .addParameter(ParameterSpec.builder(velocityProxyServerClass, "proxyServer").build())
                .addParameter(ParameterSpec.builder(loggerClass, "slf4jLogger").build())
                .addParameter(ParameterSpec.builder(velocityPluginDescriptionClass, "velocityPluginDescription").build())
                .addStatement("this.$N = new $T()", "pluginControllable", ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"));


        var serviceInitGenerator = ServiceInitGenerator
                .builder(constructorBuilder)
                .add(velocityProxyServerClass.canonicalName(), (statement, objects) -> {
                    statement.append("$N");
                    objects.add("proxyServer");
                })
                .add("com.velocitypowered.api.plugin.PluginManager", (statement, objects) -> {
                    statement.append("$N.getPluginManager()");
                    objects.add("proxyServer");
                });

        serviceInitGenerator.process(pluginManager.get());

        constructorBuilder.addStatement("$T $N = $N.getId()", String.class, "name", "velocityPluginDescription")
                .addStatement("$T $N = $T.createKey($N).orElseThrow()", pluginKeyClass, "key", pluginManagerClass, "name")
                .addStatement("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key")
                .addStatement("this.$N = new $T()", "pluginContainer", pluginContainer)
                .addStatement("this.$N.init($N, $N)", "pluginContainer", "description", "slf4jLogger");

        var onEnableBuilder = MethodSpec.methodBuilder("onEnable")
                .addAnnotation(ClassName.get("com.velocitypowered.api.event", "Subscribe"))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.get("com.velocitypowered.api.event.proxy", "ProxyInitializeEvent"), "event").build())
                .returns(void.class);

        var onDisableBuilder = MethodSpec.methodBuilder("onDisable")
                .addAnnotation(ClassName.get("com.velocitypowered.api.event", "Subscribe"))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassName.get("com.velocitypowered.api.event.proxy", "ProxyShutdownEvent"), "event").build())
                .returns(void.class);

        sorted.forEach(serviceInitGenerator::process);

        constructorBuilder
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

        if (processingEnvironment.getElementUtils().getTypeElement("org.screamingsandals.lib.event.EventManager") != null) {
            onDisableBuilder
                    .addStatement("$T.getDefaultEventManager().unregisterAll()", ClassName.get("org.screamingsandals.lib.event", "EventManager"));
        }

        var velocityMainClass = TypeSpec.classBuilder(pluginContainer.getSimpleName() + "_VelocityImpl")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec
                        .builder(TypeName.get(pluginContainer.asType()), "pluginContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"), "pluginControllable",
                                Modifier.PRIVATE)
                        .build())
                .addMethod(constructorBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        JavaFile.builder(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString(), velocityMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = GsonConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "velocity-plugin.json").toUri()))
                .build();


        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("main").set(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString() + "." + pluginContainer.getSimpleName() + "_VelocityImpl");
        node.node("id").set(pluginAnnotation.id());
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
