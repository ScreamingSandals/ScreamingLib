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

package org.screamingsandals.lib.attribute;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.AttributeTypeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService
public abstract class AttributeTypeMapping extends AbstractTypeMapper<AttributeTypeHolder> {
    private static AttributeTypeMapping attributeTypeMapping;

    protected final BidirectionalConverter<AttributeTypeHolder> attributeTypeConverter = BidirectionalConverter.<AttributeTypeHolder>build()
            .registerP2W(AttributeTypeHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return AttributeTypeHolderSerializer.INSTANCE.deserialize(AttributeTypeHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public AttributeTypeMapping() {
        if (attributeTypeMapping != null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is already initialized.");
        }

        attributeTypeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    @OfMethodAlternative(value = AttributeTypeHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable AttributeTypeHolder resolve(@Nullable Object attributeType) {
        if (attributeTypeMapping == null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is not initialized yet.");
        }

        if (attributeType == null) {
            return null;
        }

        return attributeTypeMapping.attributeTypeConverter.convertOptional(attributeType).or(() -> {
            var namespacedKey = AttributeMappingKey.ofOptional(attributeType.toString());

            if (namespacedKey.isPresent() && attributeTypeMapping.mapping.containsKey(namespacedKey.get())) {
                return Optional.of(attributeTypeMapping.mapping.get(namespacedKey.get()));
            }

            return Optional.empty();
        }).orElse(null);
    }

    @OfMethodAlternative(value = AttributeTypeHolder.class, methodName = "all")
    public static List<AttributeTypeHolder> getValues() {
        if (attributeTypeMapping == null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(attributeTypeMapping.values);
    }
}
