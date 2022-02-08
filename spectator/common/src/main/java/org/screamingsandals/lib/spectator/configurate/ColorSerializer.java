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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ColorSerializer implements TypeSerializer<Color> {
    public static final ColorSerializer INSTANCE = new ColorSerializer();

    private final String RED_KEY = "red";
    private final String RED_KEY_LEGACY_BUKKIT = "RED";
    private final String GREEN_KEY = "green";
    private final String GREEN_KEY_LEGACY_BUKKIT = "GREEN";
    private final String BLUE_KEY = "blue";
    private final String BLUE_KEY_LEGACY_BUKKIT = "BLUE";

    @Override
    public Color deserialize(Type type, ConfigurationNode node) throws SerializationException {
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
    public void serialize(Type type, @Nullable Color color, ConfigurationNode node) throws SerializationException {
        node.set(color == null ? null : color.toString());
    }
}
