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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ColorSerializer implements TypeSerializer<Color> {
    public static final @NotNull ColorSerializer INSTANCE = new ColorSerializer();

    private static final @NotNull String RED_KEY = "red";
    private static final @NotNull String RED_KEY_LEGACY_BUKKIT = "RED";
    private static final @NotNull String GREEN_KEY = "green";
    private static final @NotNull String GREEN_KEY_LEGACY_BUKKIT = "GREEN";
    private static final @NotNull String BLUE_KEY = "blue";
    private static final @NotNull String BLUE_KEY_LEGACY_BUKKIT = "BLUE";

    @Override
    public @NotNull Color deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            // TODO: numerical values to match Adventure
            if (node.isMap()) {
                return Color.rgb(
                        node.node(RED_KEY).getInt(node.node(RED_KEY_LEGACY_BUKKIT).getInt()),
                        node.node(GREEN_KEY).getInt(node.node(GREEN_KEY_LEGACY_BUKKIT).getInt()),
                        node.node(BLUE_KEY).getInt(node.node(BLUE_KEY_LEGACY_BUKKIT).getInt())
                );
            } else {
                var color = node.getString("");
                return Color.hexOrName(color);
            }
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Color color, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(color == null ? null : color.toString());
    }
}
