package org.screamingsandals.lib.annotation;

import com.squareup.javapoet.*;
import lombok.Getter;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AutoInitialization;
import org.screamingsandals.lib.utils.annotations.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes({
        "org.screamingsandals.lib.utils.annotations.Plugin",
        "org.screamingsandals.lib.utils.annotations.AutoInitialization"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@Getter
public class ScreamingAnnotationProcessor extends AbstractProcessor {
    private final Map<PlatformType, List<TypeElement>> platformInitiators = new HashMap<>();
    private TypeElement pluginContainer;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(AutoInitialization.class)
                .forEach(element -> {
                    if (element.getKind() != ElementKind.CLASS || element.getModifiers().contains(Modifier.ABSTRACT)) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@AutoInitialization can be applied only too non-abstract class");
                        throw new RuntimeException("@AutoInitialization can be applied only too non-abstract class");
                    }

                    var autoInitialization = element.getAnnotation(AutoInitialization.class);
                    if (!platformInitiators.containsKey(autoInitialization.platform())) {
                        platformInitiators.put(autoInitialization.platform(), new ArrayList<>());
                    }
                    platformInitiators.get(autoInitialization.platform()).add((TypeElement) element);
                });


/*
        var pluginAnnotations = roundEnv.getElementsAnnotatedWith(Plugin.class);
        if (!pluginAnnotations.isEmpty()) {
            if (annotated || pluginAnnotations.size() > 1) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only one class in project can be annotated with @Plugin");
                return true;
            }
            annotated = true;
            var element = pluginAnnotations.iterator().next();
            if (element.getKind() != ElementKind.CLASS || element.getModifiers().contains(Modifier.ABSTRACT)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Plugin can be applied only too non-abstract class");
                return true;
            }
            var type = (TypeElement) element;

            var bukkitPluginManagerClass = ClassName.get("org.screamingsandals.lib.bukkit.plugin", "BukkitPluginManager");
            var pluginManagerClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginManager");
            var pluginDescriptionClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginDescription");
            var pluginKeyClass = ClassName.get("org.screamingsandals.lib.plugin", "PluginKey");

            var bukkitImpl = TypeSpec.classBuilder(type.getSimpleName().toString() + "_BukkitImpl")
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get("org.bukkit.plugin.java", "JavaPlugin"))
                    .addField(
                            FieldSpec
                                    .builder(ClassName.get("org.screamingsandals.lib.plugin", "PluginContainer"), "pluginContainer")
                                    .addModifiers(Modifier.PRIVATE)
                                    .build()
                    ).addMethod(
                            MethodSpec.methodBuilder("onLoad")
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(void.class)
                                    .addStatement(CodeBlock.builder()
                                            .add("$T.init(this)", bukkitPluginManagerClass)

                                            // todo: initializers

                                            .add("$T $N = this.getName()", String.class, "name")
                                            .add("$T $N = $T.createKey($N)", pluginKeyClass, "key", pluginManagerClass, "name")
                                            .add("$T $N = $T.getPlugin($N).orElseThrow()", pluginDescriptionClass, "description", pluginManagerClass, "key")

                                            .add("$N.init($N, this.getLogger())", "pluginContainer", "description")

                                            .add("$N.load()", "pluginContainer")
                                            .build())
                                    .build()
                    ).addMethod(
                            MethodSpec.methodBuilder("onEnable")
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(void.class)
                                    .addStatement("$N.enable()", "pluginContainer")
                                    .build()
                    ).addMethod(
                            MethodSpec.methodBuilder("onDisable")
                                    .addModifiers(Modifier.PUBLIC)
                                    .returns(void.class)
                                    .addStatement("$N.disable()", "pluginContainer")
                                    .build()
                    ).build();
        }*/
        return true;
    }
}