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

import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.PlatformType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.QualifiedNameable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class MainClassGenerator {
    public abstract void generate(@NotNull ProcessingEnvironment processingEnvironment, @NotNull QualifiedNameable pluginContainer, @NotNull List<@NotNull ServiceContainer> autoInit) throws IOException;

    protected @NotNull Pair<@NotNull List<@NotNull ServiceContainer>, @NotNull List<@NotNull ServiceContainer>> sortServicesAndGetDependencies(@NotNull ProcessingEnvironment processingEnvironment, @NotNull List<@NotNull ServiceContainer> autoInit, PlatformType platformType) {
        // TODO: probably rewrite this f*cking sh*t
        var checkedDeps = new ArrayList<ServiceContainer>();
        var provided = new ArrayList<ServiceContainer>();
        var sorted = new ArrayList<ServiceContainer>();
        var waiting = new ArrayList<>(autoInit);
        while (!waiting.isEmpty()) {
            var copy = List.copyOf(waiting);
            waiting.clear();
            if (checkedDeps.containsAll(copy) && !provided.isEmpty()) {
                throw new UnsupportedOperationException("It is not possible to create a list of services: some @ProvidedServices remain unimplemented! "
                        + provided.stream().map(s -> s.getService().getQualifiedName().toString()).collect(Collectors.joining(", ")));
            }
            var delayEverything = new AtomicBoolean(false);
            copy.forEach(serviceContainer -> {
                // if this is @ProvidedService, just save it to provided collection
                if (serviceContainer.isProvided()) {
                    provided.add(serviceContainer);
                    return;
                }
                // If this implements any provided service, remove the provided service from the collection of provided services
                provided.stream().filter(s -> s.is(serviceContainer.getService())).findFirst()
                        .ifPresent(provided::remove);

                var dependencies = serviceContainer.getDependencies();
                var loadAfter = serviceContainer.getLoadAfter();
                serviceContainer.getInit()
                        .stream()
                        .filter(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement))
                                && copy.stream().noneMatch(s -> s.is(typeElement))
                                && waiting.stream().noneMatch(s -> s.is(typeElement))
                                && provided.stream().noneMatch(s -> s.isExactly(typeElement))
                        )
                        .map(typeElement ->
                                MiscUtils.getAllSpecificPlatformImplementations(processingEnvironment,
                                        typeElement,
                                        List.of(platformType),
                                        true
                                ).get(platformType))
                        .forEach(waiting::add);

                if (!checkedDeps.contains(serviceContainer)) {
                    checkedDeps.add(serviceContainer);
                }

                if (!dependencies.isEmpty() && dependencies.stream().anyMatch(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement)))) {
                    dependencies.stream()
                            .filter(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement))
                                    && copy.stream().noneMatch(s -> s.is(typeElement))
                                    && waiting.stream().noneMatch(s -> s.is(typeElement))
                                    && provided.stream().noneMatch(s -> s.isExactly(typeElement)))
                            .map(typeElement -> MiscUtils.getAllSpecificPlatformImplementations(processingEnvironment,
                                    typeElement,
                                    List.of(platformType),
                                    true
                                    ).get(platformType)
                            )
                            .forEach(waiting::add);
                    waiting.add(serviceContainer);
                    if (serviceContainer.isCoreService()) {
                        delayEverything.set(true);
                    }
                } else if (!loadAfter.isEmpty()
                        && loadAfter.stream().anyMatch(typeElement -> sorted.stream().noneMatch(s -> s.is(typeElement))
                        && (copy.stream().anyMatch(s -> s.is(typeElement)) || waiting.stream().anyMatch(s -> s.is(typeElement)) || provided.stream().anyMatch(s -> s.isExactly(typeElement))))) {
                    waiting.add(serviceContainer);
                    if (serviceContainer.isCoreService()) {
                        delayEverything.set(true);
                    }
                } else {
                    if (delayEverything.get() && !serviceContainer.isEarlyInitialization()) { // only early initialization (PluginManager) is exception
                        waiting.add(serviceContainer);
                    } else {
                        sorted.add(serviceContainer);
                    }
                }
            });
        }

        if (!provided.isEmpty()) {
            throw new UnsupportedOperationException("It is not possible to create a list of services: some @ProvidedServices remain unimplemented! "
                    + provided.stream().map(s -> s.getService().getQualifiedName().toString()).collect(Collectors.joining(", ")));
        }

        var earlyInitialization = new ArrayList<ServiceContainer>();

        sorted.removeIf(serviceContainer -> {
            if (serviceContainer.isEarlyInitialization()) {
                earlyInitialization.add(serviceContainer);
                return true;
            }
            return false;
        });

        return Pair.of(earlyInitialization, sorted);
    }

    protected MethodSpec.@NotNull Builder preparePublicVoid(@NotNull String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
    }
}
