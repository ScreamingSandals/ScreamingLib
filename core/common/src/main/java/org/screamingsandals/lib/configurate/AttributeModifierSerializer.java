/*
 * Copyright 2025 ScreamingSandals
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
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;

public class AttributeModifierSerializer implements TypeSerializer<AttributeModifier> {
    private static final @NotNull String ID_KEY = "id";
    private static final @NotNull String UUID_KEY = "uuid";
    private static final @NotNull String NAME_KEY = "name";
    private static final @NotNull String AMOUNT_KEY = "amount";
    private static final @NotNull String OPERATION_KEY = "operation";

    public static final @NotNull AttributeModifierSerializer INSTANCE = new AttributeModifierSerializer();

    @Override
    public @NotNull AttributeModifier deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var id = node.node(ID_KEY);
            var uuid = node.node(UUID_KEY);
            var name = node.node(NAME_KEY);
            var amount = node.node(AMOUNT_KEY);
            var operation = node.node(OPERATION_KEY);

            if (!id.empty()) {
                return new AttributeModifier(
                        ResourceLocation.of(id.getString("")),
                        amount.getDouble(),
                        read(operation.getString(""), AttributeModifier.Operation.ADD_VALUE)
                );
            }

            return new AttributeModifier(
                    uuid.get(UUID.class, (Supplier<UUID>) UUID::randomUUID),
                    name.getString(""),
                    amount.getDouble(),
                    read(operation.getString(""), AttributeModifier.Operation.ADD_VALUE)
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

        if (Server.isVersion(1, 21)) {
            node.node(ID_KEY).set(obj.getLocation().toString());
        } else {
            node.node(UUID_KEY).set(obj.getUuid());
            node.node(NAME_KEY).set(obj.getName());
        }
        node.node(AMOUNT_KEY).set(obj.getAmount());
        node.node(OPERATION_KEY).set(obj.getOperation());
    }

    public static AttributeModifier.@NotNull Operation read(@NotNull String operation, AttributeModifier.@NotNull Operation defaultVal) {
        switch (operation.toLowerCase(Locale.ROOT)) {
            case "addition":
            case "add_value":
                return AttributeModifier.Operation.ADD_VALUE;
            case "multiply_base":
            case "add_multiplied_base":
                return AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            case "multiply_total":
            case "add_multiplied_total":
                return AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            default:
                return defaultVal;
        }
    }
}
