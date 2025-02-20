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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.ShadowColor;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ShadowColorSerializer implements TypeSerializer<ShadowColor> {
    public static final @NotNull ShadowColorSerializer INSTANCE = new ShadowColorSerializer();

    private static final @NotNull String RED_KEY = "red";
    private static final @NotNull String RED_KEY_LEGACY_BUKKIT = "RED";
    private static final @NotNull String GREEN_KEY = "green";
    private static final @NotNull String GREEN_KEY_LEGACY_BUKKIT = "GREEN";
    private static final @NotNull String BLUE_KEY = "blue";
    private static final @NotNull String BLUE_KEY_LEGACY_BUKKIT = "BLUE";
    private static final @NotNull String ALPHA_KEY = "alpha";
    private static final @NotNull String ALPHA_KEY_LEGACY_BUKKIT = "ALPHA";

    @Override
    public @NotNull ShadowColor deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            // TODO: numerical values to match Adventure
            if (node.isMap()) {
                return ShadowColor.rgba(
                        node.node(RED_KEY).getInt(node.node(RED_KEY_LEGACY_BUKKIT).getInt()),
                        node.node(GREEN_KEY).getInt(node.node(GREEN_KEY_LEGACY_BUKKIT).getInt()),
                        node.node(BLUE_KEY).getInt(node.node(BLUE_KEY_LEGACY_BUKKIT).getInt()),
                        node.node(ALPHA_KEY).getInt(node.node(ALPHA_KEY_LEGACY_BUKKIT).getInt(1))
                );
            } else {
                var color = node.getString("");
                return ShadowColor.hex(color);
            }
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ShadowColor color, @NotNull ConfigurationNode node) throws SerializationException {
        if (color == null) {
            node.set(null);
            return;
        }

        node.node(RED_KEY).set(color.red());
        node.node(GREEN_KEY).set(color.green());
        node.node(BLUE_KEY).set(color.blue());
        node.node(ALPHA_KEY).set(color.alpha());
    }
}
