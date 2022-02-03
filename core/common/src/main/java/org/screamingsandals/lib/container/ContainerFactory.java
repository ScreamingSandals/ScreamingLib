/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.container;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;

@AbstractService
public abstract class ContainerFactory {

    private static ContainerFactory factory;

    @ApiStatus.Internal
    public ContainerFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ContainerFactory is already initialized.");
        }

        factory = this;
    }

    public static <C extends Container> Optional<C> wrapContainer(Object container) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.wrapContainer0(container);
    }

    public abstract <C extends Container> Optional<C> wrapContainer0(Object container);

    public static <C extends Container> Optional<C> createContainer(InventoryTypeHolder type) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type);
    }

    public static <C extends Container> Optional<C> createContainer(InventoryTypeHolder type, Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type, name);
    }

    public static <C extends Container> Optional<C> createContainer(int size) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size);
    }

    public static <C extends Container> Optional<C> createContainer(int size, Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size, name);
    }

    public abstract <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type);

    public abstract <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type, Component name);

    public abstract <C extends Container> Optional<C> createContainer0(int size);

    public abstract <C extends Container> Optional<C> createContainer0(int size, Component name);
}
