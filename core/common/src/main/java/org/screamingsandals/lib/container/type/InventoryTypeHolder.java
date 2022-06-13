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

package org.screamingsandals.lib.container.type;

import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface InventoryTypeHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    int size();

    /**
     * Use the fluent variant
     */
    @Deprecated(forRemoval = true)
    default int getSize() {
        return size();
    }

    default <C extends Container> Optional<C> createContainer(ComponentLike name) {
        return createContainer(name.asComponent());
    }

    default <C extends Container> Optional<C> createContainer(Component name) {
        return ContainerFactory.createContainer(this, name);
    }

    default <C extends Container> Optional<C> createContainer() {
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
    boolean is(Object object);

    /**
     * Compares the entity type and the objects
     *
     * @param objects Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    static InventoryTypeHolder of(Object inventoryType) {
        return ofOptional(inventoryType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    static Optional<InventoryTypeHolder> ofOptional(Object inventoryType) {
        if (inventoryType instanceof InventoryTypeHolder) {
            return Optional.of((InventoryTypeHolder) inventoryType);
        }
        return InventoryTypeMapping.resolve(inventoryType);
    }

    static List<InventoryTypeHolder> all() {
        return InventoryTypeMapping.getValues();
    }
}
