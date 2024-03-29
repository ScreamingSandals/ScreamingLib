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

package org.screamingsandals.lib.container;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.ContainerHolder;
import org.screamingsandals.lib.container.type.InventoryType;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.Objects;

@ProvidedService
public abstract class ContainerFactory {
    static {
        ContainerHolder.Provider.registerProvider(o ->
                Objects.requireNonNull(
                    Objects.requireNonNull(ContainerFactory.factory, "ContainerFactory is not initialized yet.").wrapContainer0(o),
                    "Cannot wrap " + o + " to Container"
                )
        );
    }


    private static @Nullable ContainerFactory factory;

    @ApiStatus.Internal
    public ContainerFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ContainerFactory is already initialized.");
        }

        factory = this;
    }

    @Contract("null -> null")
    public static <C extends Container> @Nullable C wrapContainer(@Nullable Object container) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.wrapContainer0(container);
    }

    public abstract <C extends Container> @Nullable C wrapContainer0(@Nullable Object container);

    @Contract("null -> null")
    public static <C extends Container> @Nullable C createContainer(@Nullable InventoryType type) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type);
    }

    @Contract("null, _ -> null")
    public static <C extends Container> @Nullable C createContainer(@Nullable InventoryType type, @Nullable Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type, name);
    }

    public static <C extends Container> @Nullable C createContainer(int size) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size);
    }

    public static <C extends Container> @Nullable C createContainer(int size, @Nullable Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size, name);
    }

    public abstract <C extends Container> @Nullable C createContainer0(@Nullable InventoryType type);

    public abstract <C extends Container> @Nullable C createContainer0(@Nullable InventoryType type, @Nullable Component name);

    public abstract <C extends Container> @Nullable C createContainer0(int size);

    public abstract <C extends Container> @Nullable C createContainer0(int size, @Nullable Component name);
}
