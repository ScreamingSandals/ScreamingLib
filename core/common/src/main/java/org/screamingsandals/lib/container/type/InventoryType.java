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

package org.screamingsandals.lib.container.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface InventoryType extends RegistryItem, RawValueHolder {
    @NotNull String platformName();

    int size();

    default <C extends Container> @Nullable C createContainer(@Nullable ComponentLike name) {
        return createContainer(name != null ? name.asComponent() : null);
    }

    default <C extends Container> @Nullable C createContainer(@Nullable Component name) {
        return ContainerFactory.createContainer(this, name);
    }

    default <C extends Container> @Nullable C createContainer() {
        return ContainerFactory.createContainer(this);
    }

    /**
     * Compares the inventory type and the object
     *
     * @param object Object that represents inventory type
     * @return true if specified inventory type is the same as this
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.INVENTORY_TYPE) @Nullable Object object);

    /**
     * Compares the inventory type and the objects
     *
     * @param objects Array of objects that represents inventory type
     * @return true if at least one of the inventory type objects is same as this
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.INVENTORY_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull InventoryType of(@MinecraftType(MinecraftType.Type.INVENTORY_TYPE) @NotNull Object inventoryType) {
        var result = ofNullable(inventoryType);
        Preconditions.checkNotNullIllegal(result, "Could not find inventory type: " + inventoryType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable InventoryType ofNullable( @MinecraftType(MinecraftType.Type.INVENTORY_TYPE) @Nullable Object inventoryType) {
        if (inventoryType instanceof InventoryType) {
            return (InventoryType) inventoryType;
        }
        return InventoryTypeRegistry.getInstance().resolveMapping(inventoryType);
    }

    static @NotNull RegistryItemStream<@NotNull InventoryType> all() {
        return InventoryTypeRegistry.getInstance().getRegistryItemStream();
    }
}
