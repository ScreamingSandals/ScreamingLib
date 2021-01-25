package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.*;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BukkitMainClassGenerator implements MainClassGenerator {
    @Override
    public void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException {
        var sorted = new ArrayList<ServiceContainer>();
        var waiting = new ArrayList<>(autoInit);
        while (!waiting.isEmpty()) {
            var copy = List.copyOf(waiting);
            waiting.clear();
            copy.forEach(serviceContainer -> {
                var dependencies = serviceContainer.getDependencies();
                var loadAfter = serviceContainer.getLoadAfter();
                if (!dependencies.isEmpty() && dependencies.stream().anyMatch(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement)))) {
                    dependencies.stream()
                            .filter(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement))
                                    && copy.stream().noneMatch(s -> s.is(typeElement))
                                    && waiting.stream().noneMatch(s -> s.is(typeElement)))
                            .map(typeElement -> MiscUtils.getAllSpecificPlatformImplementations(processingEnvironment,
                                    typeElement,
                                    List.of(PlatformType.BUKKIT),
                                    true
                                    ).get(PlatformType.BUKKIT)
                            )
                            .forEach(waiting::add);
                    waiting.add(serviceContainer);
                } else if (!loadAfter.isEmpty()
                        && loadAfter.stream().anyMatch(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement))
                        && (copy.stream().anyMatch(s -> s.is(typeElement)) || waiting.stream().anyMatch(s -> s.is(typeElement))))) {
                    waiting.add(serviceContainer);
                } else {
                    sorted.add(serviceContainer);
                }
            });
        }

        var onLoadBuilder = MethodSpec.methodBuilder("onLoad")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("this.$N = new $T()", "pluginControllable", ClassName.get("org.screamingsandals.lib.utils", "Controllable"));

        var onEnableBuilder = MethodSpec.methodBuilder("onEnable")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        var onDisableBuilder = MethodSpec.methodBuilder("onDisable")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        sorted.forEach(serviceContainer -> {
            var typeElement = serviceContainer.getService();
            var initMethod = typeElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind() == ElementKind.METHOD && "init".equals(element.getSimpleName().toString()))
                    .findFirst();

            if (initMethod.isEmpty()) {
                initMethod = typeElement.getEnclosedElements()
                        .stream()
                        .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                        .findFirst();
            }

            if (initMethod.isPresent()) {
                var method = (ExecutableElement) initMethod.get();
                var arguments = method.getParameters();
                var processedArguments = new ArrayList<>();
                var statement = new StringBuilder();
                if (method.getKind() == ElementKind.CONSTRUCTOR) {
                    statement.append("new $T(");
                } else {
                    statement.append("$T.init(");
                }
                processedArguments.add(typeElement);
                var first = new AtomicBoolean(true);
                arguments.forEach(variableElement -> {
                    if (!first.get()) {
                        statement.append(",");
                    } else {
                        first.set(false);
                    }
                    if (arguments.get(0).asType().toString().equals("org.bukkit.plugin.java.JavaPlugin") || arguments.get(0).asType().toString().equals("org.bukkit.plugin.Plugin")) {
                        statement.append("this");
                    } else if (arguments.get(0).asType().toString().equals("org.screamingsandals.lib.utils.Controllable")) {
                        statement.append("this.$N.child()");
                        processedArguments.add("pluginControllable");
                    } else if (arguments.get(0).asType().toString().equals("org.screamingsandals.lib.plugin.PluginContainer")) {
                        statement.append("this.$N");
                        processedArguments.add("pluginContainer");
                    } else {
                        throw new UnsupportedOperationException("Init method of " + typeElement.getQualifiedName() + " has wrong argument!");
                    }
                });
                statement.append(")");
                onLoadBuilder.addStatement(statement.toString(), processedArguments.toArray());
            } else {
                throw new UnsupportedOperationException("Can't auto initialize " + typeElement.getQualifiedName() + " without init method");
            }
        });

        var pluginManagerClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginManager");
        var pluginDescriptionClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginDescription");
        var pluginKeyClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginKey");

        onLoadBuilder
                .addStatement("$T $N = this.getName()", String.class, "name")
                .addStatement("$T $N = $T.createKey($N).orElseThrow()", pluginKeyClass, "key", pluginManagerClass, "name")
                .addStatement("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key")
                .addStatement("this.$N = new $T()", "pluginContainer", pluginContainer)
                .addStatement("this.$N.init($N, this.getLogger())", "pluginContainer", "description")
                .addStatement("this.$N.load()", "pluginContainer");

        onEnableBuilder
                .beginControlFlow("if (this.$N == null)", "pluginContainer")
                .addStatement("throw new $T($S)", UnsupportedOperationException.class, "Plugin must be loaded before enabling!")
                .endControlFlow()
                .addStatement("this.$N.enable()", "pluginControllable")
                .addStatement("this.$N.enable()", "pluginContainer");

        onDisableBuilder
                .addStatement("this.$N.disable()", "pluginContainer")
                .addStatement("this.$N.disable()", "pluginControllable");

        var bukkitMainClass = TypeSpec.classBuilder(pluginContainer.getSimpleName() + "_BukkitImpl")
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.get("org.bukkit.plugin.java", "JavaPlugin"))
                .addField(FieldSpec
                        .builder(TypeName.get(pluginContainer.asType()), "pluginContainer", Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec
                        .builder(ClassName.get("org.screamingsandals.lib.utils", "Controllable"), "pluginControllable",
                                Modifier.PRIVATE)
                        .build())
                .addMethod(onLoadBuilder.build())
                .addMethod(onEnableBuilder.build())
                .addMethod(onDisableBuilder.build())
                .build();

        JavaFile.builder(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString(), bukkitMainClass)
                .build()
                .writeTo(processingEnvironment.getFiler());

        var loader = YamlConfigurationLoader.builder()
                .path(Path.of(processingEnvironment.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml").toUri()))
                .build();


        var pluginAnnotation = pluginContainer.getAnnotation(Plugin.class);

        var node = loader.createNode();
        node.node("main").set(((PackageElement) pluginContainer.getEnclosingElement()).getQualifiedName().toString() + "." + pluginContainer.getSimpleName() + "_BukkitImpl");
        node.node("name").set(pluginAnnotation.id());
        node.node("version").set(pluginAnnotation.version());
        node.node("api-version").set("1.13");
        node.node("load").set(pluginAnnotation.loadTime().name());
        if (!pluginAnnotation.name().isBlank()) {
            node.node("prefix").set(pluginAnnotation.name());
        }
        if (!pluginAnnotation.description().isBlank()) {
            node.node("description").set(pluginAnnotation.description());
        }
        if (pluginAnnotation.authors().length > 0) {
            node.node("authors").set(Arrays.asList(pluginAnnotation.authors()));
        }

        var depend = new ArrayList<String>();
        var softdepend = new ArrayList<String>();
        var loadbefore = new ArrayList<String>();

        Arrays.stream(pluginContainer.getAnnotationsByType(PluginDependencies.class)).forEach(pluginDependencies -> {
            if (pluginDependencies.platform() == PlatformType.BUKKIT) {
                depend.addAll(Arrays.asList(pluginDependencies.dependencies()));
                softdepend.addAll(Arrays.asList(pluginDependencies.softDependencies()));
                loadbefore.addAll(Arrays.asList(pluginDependencies.loadBefore()));
            }
        });

        if (!depend.isEmpty()) {
            node.node("depend").set(depend.stream().distinct().collect(Collectors.toList()));
        }

        if (!softdepend.isEmpty()) {
            node.node("softdepend").set(softdepend.stream().distinct().collect(Collectors.toList()));
        }

        if (!loadbefore.isEmpty()) {
            node.node("loadbefore").set(loadbefore.stream().distinct().collect(Collectors.toList()));
        }

        loader.save(node);

    }
}
