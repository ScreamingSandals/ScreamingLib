package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.*;
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

public class VelocityMainClassGenerator extends MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sortedPair = sortServicesAndGetDependencies(processingEnvironment, autoInit, PlatformType.VELOCITY);

        var pluginManagerClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginManager");
        var pluginDescriptionClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginDescription");
        var pluginKeyClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginKey");
        var screamingLoggerClass = ClassName.get("org.screamingsandals.lib.utils.logger", "Slf4jLoggerWrapper");
        var loggerClass = ClassName.get("org.slf4j", "Logger");

        var velocityProxyServerClass = ClassName.get("com.velocitypowered.api.proxy", "ProxyServer");
        var guiceInjectorClass = ClassName.get("com.google.inject", "Injector");

        var constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("com.google.inject", "Inject"))
                .addParameter(ParameterSpec.builder(velocityProxyServerClass, "proxyServer").build())
                .addParameter(ParameterSpec.builder(loggerClass, "slf4jLogger").build())
                .addParameter(ParameterSpec.builder(guiceInjectorClass, "guice").build())
                .addStatement("this.$N = new $T()", "pluginControllable", ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"))
                .addStatement("this.$N = $N", "guiceInjector", "guice")
                .addStatement("this.$N = new $T($N)", "screamingLogger", screamingLoggerClass, "slf4jLogger");


        var serviceInitGenerator = ServiceInitGenerator
                .builder(constructorBuilder, processingEnvironment.getTypeUtils(), processingEnvironment.getElementUtils())
                .add(velocityProxyServerClass.canonicalName(), (statement, objects) -> {
                    statement.append("$N");
                    objects.add("proxyServer");
                })
                .add("com.velocitypowered.api.plugin.PluginManager", (statement, objects) -> {
                    statement.append("$N.getPluginManager()");
                    objects.add("proxyServer");
                })
                .add("java.lang.Object", (statement, objects) -> // probably plugin
                        statement.append("this")
                )
                .add(pluginContainer.asType().toString(), (statement, processedArguments) -> {
                    statement.append("this.$N");
                    processedArguments.add("pluginContainer");
                });

        sortedPair.getFirst().forEach(serviceInitGenerator::process);

        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);
        var onEnableBuilder = preparePublicVoid("onEnable")
                .addAnnotation(ClassName.get("com.velocitypowered.api.event", "Subscribe"))
                .addParameter(ParameterSpec.builder(ClassName.get("com.velocitypowered.api.event.proxy", "ProxyInitializeEvent"), "event").build());

        var onDisableBuilder = preparePublicVoid("onDisable")
                .addAnnotation(ClassName.get("com.velocitypowered.api.event", "Subscribe"))
                .addParameter(ParameterSpec.builder(ClassName.get("com.velocitypowered.api.event.proxy", "ProxyShutdownEvent"), "event").build());


        sortedPair.getSecond().forEach(serviceInitGenerator::process);

        //build container
        onEnableBuilder.addStatement("$T $N = $T.createKey($S).orElseThrow()", pluginKeyClass, "key", pluginManagerClass, pluginAnnotation.id())
                .addStatement("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key");

        if (pluginContainer.getEnclosedElements().stream().filter(element -> element.getKind() == ElementKind.CONSTRUCTOR).map(el -> (ExecutableElement) el).anyMatch(el -> el.getParameters().size() == 1 && el.getParameters().get(0).asType().toString().equals("com.google.inject.Injector"))) {
            onEnableBuilder.addStatement("this.$N = new $T(guiceInjector)", "pluginContainer", pluginContainer);
        } else {
            onEnableBuilder.addStatement("this.$N = new $T()", "pluginContainer", pluginContainer);
        }

        //init
        onEnableBuilder
                .addStatement("this.$N.init($N, $N)", "pluginContainer", "description", "screamingLogger");

        //load the container first
        onEnableBuilder
                .addStatement("this.$N.load()", "pluginContainer");

        //then enable
        onEnableBuilder.addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.enable()", "pluginContainer")
                .addStatement("this.$N.postEnable()", "pluginControllable");

        onDisableBuilder
                .addStatement("this.$N.preDisable()", "pluginControllable")
                .addStatement("this.$N.disable()", "pluginContainer")
                .addStatement("this.$N.disable()", "pluginControllable");

        var velocityMainClass = TypeSpec.classBuilder(pluginContainer.getSimpleName() + "_VelocityImpl")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec
                        .builder(TypeName.get(pluginContainer.asType()), "pluginContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl"), "pluginControllable",
                                Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(screamingLoggerClass, "screamingLogger", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(guiceInjectorClass, "guiceInjector", Modifier.PRIVATE)
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


        var node = loader.createNode();
        node.node("main").set(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString() + "." + pluginContainer.getSimpleName() + "_VelocityImpl");
        node.node("id").set(pluginAnnotation.id().toLowerCase());
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
