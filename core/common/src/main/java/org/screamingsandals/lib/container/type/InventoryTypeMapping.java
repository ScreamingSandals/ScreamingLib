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

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.InventoryTypeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
// TODO: Bukkit's inventory types doesn't exactly match the vanilla types
public abstract class InventoryTypeMapping extends AbstractTypeMapper<InventoryTypeHolder> {
    private static InventoryTypeMapping inventoryTypeMapping;

    protected final BidirectionalConverter<InventoryTypeHolder> inventoryTypeConverter = BidirectionalConverter.<InventoryTypeHolder>build()
            .registerP2W(InventoryTypeHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return InventoryTypeHolderSerializer.INSTANCE.deserialize(InventoryTypeHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public InventoryTypeMapping() {
        if (inventoryTypeMapping != null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is already initialized.");
        }
        inventoryTypeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    @OfMethodAlternative(value = InventoryTypeHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable InventoryTypeHolder resolve(@Nullable Object entity) {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }

        if (entity == null) {
            return null;
        }

        return inventoryTypeMapping.inventoryTypeConverter.convertOptional(entity).or(() -> inventoryTypeMapping.resolveFromMapping(entity)).toNullable();
    }

    @OfMethodAlternative(value = InventoryTypeHolder.class, methodName = "all")
    public static @NotNull List<@NotNull InventoryTypeHolder> getValues() {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(inventoryTypeMapping.values);
    }
}
