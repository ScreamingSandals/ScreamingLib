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

package org.screamingsandals.lib.impl.attribute;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.configurate.AttributeModifierSerializer;
import org.screamingsandals.lib.configurate.ItemAttributeSerializer;
import org.screamingsandals.lib.impl.slot.EquipmentSlotRegistry;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.function.Function;

@ProvidedService
@ApiStatus.Internal
@ServiceDependencies(dependsOn = {
        AttributeTypeRegistry.class,
        EquipmentSlotRegistry.class
})
public abstract class Attributes {
    private static @Nullable Attributes attributeMapping;
    private static final @NotNull Function<@NotNull ConfigurationNode, @Nullable AttributeModifier> CONFIGURATE_LOAD_MODIFIER = node -> {
        try {
            return AttributeModifierSerializer.INSTANCE.deserialize(AttributeModifier.class, node);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };
    private static final @NotNull Function<@NotNull ConfigurationNode, @Nullable ItemAttribute> CONFIGURATE_LOAD_ITEM = node -> {
        try {
            return ItemAttributeSerializer.INSTANCE.deserialize(ItemAttribute.class, node);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        return null;
    };

    protected final @NotNull BidirectionalConverter<AttributeModifier> attributeModifierConverter = BidirectionalConverter.<AttributeModifier>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_MODIFIER)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_MODIFIER.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            })
            .registerP2W(AttributeModifier.class, e -> e);

    protected final @NotNull BidirectionalConverter<ItemAttribute> itemAttributeConverter = BidirectionalConverter.<ItemAttribute>build()
            .registerP2W(ConfigurationNode.class, CONFIGURATE_LOAD_ITEM)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_LOAD_ITEM.apply(BasicConfigurationNode.root().set(map));
                } catch (ConfigurateException ignored) {
                    return null;
                }
            })
            .registerP2W(ItemAttribute.class, e -> e);

    public Attributes() {
        if (attributeMapping != null) {
            throw new UnsupportedOperationException("AttributeMapping is already initialized.");
        }

        attributeMapping = this;
    }

    @Contract("null -> null")
    public static @Nullable Attribute wrapAttribute(@Nullable Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.wrapAttribute0(attribute);
    }

    protected abstract @Nullable Attribute wrapAttribute0(@Nullable Object attribute);

    @Contract("null -> null")
    public static @Nullable AttributeModifier wrapAttributeModifier(@Nullable Object attributeModifier) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convertNullable(attributeModifier);
    }

    @Contract("null -> null")
    public static @Nullable ItemAttribute wrapItemAttribute(@Nullable Object attribute) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convertNullable(attribute);
    }

    public static <T> T convertItemAttributeHolder(@NotNull ItemAttribute holder, @NotNull Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.itemAttributeConverter.convert(holder, newType);
    }

    public static <T> T convertAttributeModifierHolder(@NotNull AttributeModifier holder, @NotNull Class<T> newType) {
        if (attributeMapping == null) {
            throw new UnsupportedOperationException("AttributeMapping is not initialized yet.");
        }
        return attributeMapping.attributeModifierConverter.convert(holder, newType);
    }
}
