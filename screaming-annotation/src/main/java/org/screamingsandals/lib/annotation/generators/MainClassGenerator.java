package org.screamingsandals.lib.annotation.generators;

import com.squareup.javapoet.MethodSpec;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.PlatformType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MainClassGenerator {
    public abstract void generate(ProcessingEnvironment processingEnvironment, TypeElement pluginContainer, List<ServiceContainer> autoInit) throws IOException;

    protected Pair<List<ServiceContainer>, List<ServiceContainer>> sortServicesAndGetDependencies(ProcessingEnvironment processingEnvironment, List<ServiceContainer> autoInit, PlatformType platformType) {
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
                                    List.of(platformType),
                                    true
                                    ).get(platformType)
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

        var earlyInitialization = new ArrayList<ServiceContainer>();

        sorted.removeIf(serviceContainer -> {
            if (serviceContainer.isEarlyInitialization()) {
                earlyInitialization.add(serviceContainer);
                return true;
            }
            return false;
        });

        return new Pair<>(earlyInitialization, sorted);
    }

    protected MethodSpec.Builder preparePublicVoid(String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
    }
}
