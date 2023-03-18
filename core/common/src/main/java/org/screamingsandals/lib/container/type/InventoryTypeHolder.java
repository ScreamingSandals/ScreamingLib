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
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

public interface InventoryTypeHolder extends ComparableWrapper, RawValueHolder {
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
     * Compares the entity type and the object
     *
     * @param object Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    boolean is(@Nullable Object object);

    /**
     * Compares the entity type and the objects
     *
     * @param objects Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    static @NotNull InventoryTypeHolder of(@NotNull Object inventoryType) {
        var result = ofNullable(inventoryType);
        Preconditions.checkNotNullIllegal(result, "Could not find inventory type: " + inventoryType);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    @Contract("null -> null")
    static @Nullable InventoryTypeHolder ofNullable(@Nullable Object inventoryType) {
        if (inventoryType instanceof InventoryTypeHolder) {
            return (InventoryTypeHolder) inventoryType;
        }
        return InventoryTypeMapping.resolve(inventoryType);
    }

    static @Unmodifiable @NotNull List<@NotNull InventoryTypeHolder> all() {
        return InventoryTypeMapping.getValues();
    }
}
