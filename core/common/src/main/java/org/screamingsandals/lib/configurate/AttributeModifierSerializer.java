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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Supplier;

public class AttributeModifierSerializer implements TypeSerializer<AttributeModifier> {
    private static final @NotNull String UUID_KEY = "uuid";
    private static final @NotNull String NAME_KEY = "name";
    private static final @NotNull String AMOUNT_KEY = "amount";
    private static final @NotNull String OPERATION_KEY = "operation";

    public static final @NotNull AttributeModifierSerializer INSTANCE = new AttributeModifierSerializer();

    @Override
    public @NotNull AttributeModifier deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var uuid = node.node(UUID_KEY);
            var name = node.node(NAME_KEY);
            var amount = node.node(AMOUNT_KEY);
            var operation = node.node(OPERATION_KEY);

            return new AttributeModifier(
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    operation.get(AttributeModifier.Operation.class, AttributeModifier.Operation.ADDITION)
            );
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable AttributeModifier obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(UUID_KEY).set(obj.getUuid());
        node.node(NAME_KEY).set(obj.getName());
        node.node(AMOUNT_KEY).set(obj.getAmount());
        node.node(OPERATION_KEY).set(obj.getOperation());
    }
}
