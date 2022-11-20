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

package org.screamingsandals.lib.attribute;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("AlternativeMethodAvailable")
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
@AbstractService
@ServiceDependencies(dependsOn = {
        AttributeTypeMapping.class,
        EquipmentSlotMapping.class
})
public abstract class AttributeMapping {
    private static AttributeMapping attributeMapping;
    private static final Function<ConfigurationNode, AttributeModifierHolder> CONFIGURATE_LOAD_MODIFIER = node -> {
        var uuid = node.node("uuid");
        var name = node.node("name");
        var amount = node.node("amount");
        var operation = node.node("operation");

        try {
            return new AttributeModifierHolder(
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    operation.get(AttributeModifierHolder.Operation.class)
            );
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };
    private static final Function<ConfigurationNode, ItemAttributeHolder> CONFIGURATE_LOAD_ITEM = node -> {
        var type = node.node("type");
        var uuid = node.node("uuid");
        var name = node.node("name");
        var amount = node.node("amount");
        var operation = node.node("operation");
        var slot = node.node("slot");

        var typeOpt = AttributeTypeMapping.resolve(type.raw());

        if (typeOpt == null) {
            return null;
        }

        try {
            return new ItemAttributeHolder(
                    typeOpt,
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    operation.get(AttributeModifierHolder.Operation.class),
                    EquipmentSlotMapping.resolve(slot.raw())
            );
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };

    protected final BidirectionalConverter<AttributeModifierHolder> attributeModifierConverter = BidirectionalConverter.<AttributeModifierHolder>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_MODIFIER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_MODIFIER.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            })
            .registerP2W(AttributeModifierHolder.class, e -> e);

    protected final BidirectionalConverter<ItemAttributeHolder> itemAttributeConverter = BidirectionalConverter.<ItemAttributeHolder>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_ITEM)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_ITEM.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            })
            .registerP2W(ItemAttributeHolder.class, e -> e);

    @ApiStatus.Internal
    public AttributeMapping() {
        if (attributeMapping != null) {
            throw new UnsupportedOperationException("AttributeMapping is already initialized.");
        }

        attributeMapping = this;
    }

    @Contract("null -> null")
    public static @Nullable AttributeHolder wrapAttribute(@Nullable Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.wrapAttribute0(attribute);
    }

    protected abstract @Nullable AttributeHolder wrapAttribute0(@Nullable Object attribute);

    @Contract("null -> null")
    public static @Nullable AttributeModifierHolder wrapAttributeModifier(@Nullable Object attributeModifier) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convertOptional(attributeModifier).toNullable();
    }

    @Contract("null -> null")
    public static @Nullable ItemAttributeHolder wrapItemAttribute(@Nullable Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convertOptional(attribute).toNullable();
    }

    public static <T> T convertItemAttributeHolder(ItemAttributeHolder holder, Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convert(holder, newType);
    }

    public static <T> T convertAttributeModifierHolder(AttributeModifierHolder holder, Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convert(holder, newType);
    }
}
