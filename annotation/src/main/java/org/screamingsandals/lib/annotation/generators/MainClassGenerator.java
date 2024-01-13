/*
 * Copyright 2024 ScreamingSandals
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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.annotation.utils.GraphUtils;
import org.screamingsandals.lib.annotation.utils.JohnsonSimpleCycles;
import org.screamingsandals.lib.annotation.utils.MiscUtils;
import org.screamingsandals.lib.annotation.utils.ServiceContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.impl.utils.Triple;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MainClassGenerator {
    public abstract void generate(@NotNull ProcessingEnvironment processingEnvironment, @NotNull QualifiedNameable pluginContainer, @NotNull List<@NotNull ServiceContainer> autoInit) throws IOException;

    @SuppressWarnings("UnstableApiUsage")
    protected @NotNull Triple<@NotNull List<@NotNull ServiceContainer>, @NotNull List<@NotNull ServiceContainer>, @NotNull Set<@NotNull String>> sortServicesAndGetDependencies(@NotNull ProcessingEnvironment processingEnvironment, @NotNull List<@NotNull ServiceContainer> autoInit, PlatformType platformType) {

        // TODO: remove early initialized (probably no longer needed)

        var registered = new ArrayList<>(autoInit);
        var waiting = new ArrayList<>(autoInit);
        var provided = new ArrayList<ServiceContainer>();
        var coreServices = new ArrayList<ServiceContainer>();
        var graph = GraphBuilder.directed().incidentEdgeOrder(ElementOrder.stable()).<ServiceContainer>build();
        var unsatisfiedDependency = ArrayListMultimap.<TypeElement, ServiceContainer>create();
        var unsatisfiedInit = ArrayListMultimap.<TypeElement, ServiceContainer>create();
        var ignorableUnsatisfiedDependency = ArrayListMultimap.<TypeElement, ServiceContainer>create();


        for (var service : autoInit) {
            // if this is @ProvidedService, save it to the provided collection
            if (service.isProvided()) {
                provided.add(service);
            }

            if (service.isCoreService()) {
                coreServices.add(service);
            }

            graph.addNode(service);
        }

        while (!waiting.isEmpty()) {
            var copy = List.copyOf(waiting);
            waiting.clear();
            for (var service : copy) {
                // check if this service implements any provided service
                if (!service.isProvided()) {
                    var implementedProvidedService = provided.stream().filter(s -> s.is(service.getService())).findFirst();
                    if (implementedProvidedService.isPresent()) {
                        provided.remove(implementedProvidedService.get());
                        graph.putEdge(implementedProvidedService.get(), service);
                    }
                }

                // check if this newly found service satisfies any unsatisfiedDependency
                for (var it = unsatisfiedDependency.entries().iterator(); it.hasNext();) {
                    var unsatisfied = it.next();
                    if (service.is(unsatisfied.getKey())) {
                        graph.putEdge(unsatisfied.getValue(), service);
                        it.remove();
                    }
                }

                // check if this newly found service satisfies any unsatisfiedInit
                for (var it = unsatisfiedInit.entries().iterator(); it.hasNext();) {
                    var unsatisfied = it.next();
                    if (service.is(unsatisfied.getKey())) {
                        graph.putEdge(service, unsatisfied.getValue());
                        it.remove();
                    }
                }

                // check if this newly found service satisfies any ignorableUnsatisfiedDependency
                for (var it = ignorableUnsatisfiedDependency.entries().iterator(); it.hasNext();) {
                    var unsatisfied = it.next();
                    if (service.is(unsatisfied.getKey())) {
                        graph.putEdge(unsatisfied.getValue(), service);
                        it.remove();
                    }
                }

                // check init services
                for (var dep : service.getInit()) {
                    var existingService = registered.stream().filter(s -> s.is(dep)).findFirst();
                    if (existingService.isPresent()) {
                        graph.putEdge(service, existingService.get());
                    } else {
                        var newService = MiscUtils.getAllSpecificPlatformImplementations(
                                processingEnvironment,
                                dep,
                                List.of(platformType),
                                true
                        ).get(platformType);
                        if (newService != null) {
                            graph.addNode(newService);
                            registered.add(newService);

                            if (newService.isProvided()) {
                                provided.add(newService);
                            }

                            if (newService.isCoreService()) {
                                coreServices.add(newService);
                            }

                            waiting.add(newService);
                            graph.putEdge(service, newService);
                        } else {
                            unsatisfiedInit.put(dep, service);
                        }
                    }
                }

                // load after
                for (var dep : service.getLoadAfter()) {
                    var existingService = registered.stream().filter(s -> s.is(dep)).findFirst();
                    if (existingService.isPresent()) {
                        graph.putEdge(existingService.get(), service);
                    } else {
                        ignorableUnsatisfiedDependency.put(dep, service);
                    }
                }

                // check dependencies
                for (var dep : service.getDependencies()) {
                    var existingService = registered.stream().filter(s -> s.is(dep)).findFirst();
                    if (existingService.isPresent()) {
                        graph.putEdge(existingService.get(), service);
                    } else {
                        var newService = MiscUtils.getAllSpecificPlatformImplementations(
                                processingEnvironment,
                                dep,
                                List.of(platformType),
                                true
                        ).get(platformType);
                        if (newService != null) {
                            graph.addNode(newService);
                            registered.add(newService);

                            if (newService.isProvided()) {
                                provided.add(newService);
                            }

                            if (newService.isCoreService()) {
                                coreServices.add(newService);
                            }

                            waiting.add(newService);
                            graph.putEdge(newService, service);
                        } else {
                            unsatisfiedDependency.put(dep, service);
                        }
                    }
                }
            }
        }

        if (!unsatisfiedInit.isEmpty()) {
            throw new UnsupportedOperationException("It is not possible to create a list of services: could not satisfy following init dependencies: "
                    + unsatisfiedInit.keys().stream().map(s -> s.getQualifiedName().toString()).collect(Collectors.joining(", ")));
        }

        if (!unsatisfiedDependency.isEmpty()) {
            throw new UnsupportedOperationException("It is not possible to create a list of services: could not satisfy following dependencies: "
                    + unsatisfiedDependency.keys().stream().map(s -> s.getQualifiedName().toString()).collect(Collectors.joining(", ")));
        }

        var transitiveClosure = Graphs.transitiveClosure(graph);

        for (var service : registered) {
            if (!service.isCoreService() && !service.isEarlyInitialization() && coreServices.stream().noneMatch(coreService -> transitiveClosure.hasEdgeConnecting(service, coreService))) {
                for (var coreService : coreServices) {
                    if (!transitiveClosure.hasEdgeConnecting(coreService, service)) {
                        graph.putEdge(coreService, service); // all regular services should depend on the core service, if they are not its dependency
                    }
                }
            }
        }

        if (!provided.isEmpty()) {
            throw new UnsupportedOperationException("It is not possible to create a list of services: some @ProvidedServices remain unimplemented! "
                    + provided.stream().map(s -> s.getService().getQualifiedName().toString()).collect(Collectors.joining(", ")));
        }

        var cycles = new JohnsonSimpleCycles<>(graph).findAndRemoveSimpleCycles();

        if (!cycles.isEmpty()) {
            System.err.println("=================================");
            System.err.println("Circular service dependency detected:");
            for (int i = 0; i < cycles.size(); i++) {
                var cycle = cycles.get(i);
                var names = cycle.stream().map(s -> s.getService().getSimpleName()).collect(Collectors.joining(" -> "));
                System.err.format("%d) %s%n", i + 1, names);
            }
            System.err.println("=================================");
            throw new UnsupportedOperationException("Circular service dependency detected");
        }


        var accessedPlugins = registered.stream().flatMap(serviceContainer -> serviceContainer.getAccessedPlugins().stream()).collect(Collectors.toSet());

        var finalIterator = GraphUtils.sortGraph(graph);

        var earlyInitialization = new ArrayList<ServiceContainer>();
        var sorted = new ArrayList<ServiceContainer>();

        for (var service : finalIterator) {
            if (service.isProvided()) {
                continue; // Provided services should be now replaced with implementation
            }

            if (service.isEarlyInitialization()) {
                earlyInitialization.add(service);
            } else {
                sorted.add(service);
            }
        }

        return Triple.of(earlyInitialization, sorted, accessedPlugins);
    }

    protected MethodSpec.@NotNull Builder preparePublicVoid(@NotNull String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
    }
}
