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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.spectator.Color;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FireworkEffectSerializer implements TypeSerializer<FireworkEffect> {
    private static final @NotNull String TYPE_KEY = "type";
    private static final @NotNull String FLICKER_KEY = "flicker";
    private static final @NotNull String TRAIL_KEY = "trail";
    private static final @NotNull String COLORS_KEY = "colors";
    private static final @NotNull String FADE_COLORS_KEY = "fade-colors";

    public static final @NotNull FireworkEffectSerializer INSTANCE = new FireworkEffectSerializer();

    @Override
    public @NotNull FireworkEffect deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (!node.isMap()) {
                return FireworkEffect.of(node.getString());
            }

            var effectNode = node.node(TYPE_KEY);
            var flickerNode = node.node(FLICKER_KEY);
            var trailNode = node.node(TRAIL_KEY);
            var colorsNode = node.node(COLORS_KEY);
            var fadeColorsNode = node.node(FADE_COLORS_KEY);

            var holder = FireworkEffect.of(effectNode.getString())
                    .withFlicker(flickerNode.getBoolean(true))
                    .withTrail(trailNode.getBoolean(true));


            if (!colorsNode.empty()) {
                if (colorsNode.isList()) {
                    holder = holder.withColors(colorsNode.childrenList()
                            .stream()
                            .map(this::deserializeColor)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else {
                    var color = deserializeColor(colorsNode);
                    if (color != null) {
                        holder = holder.withColors(List.of(color));
                    }
                }
            }

            if (!fadeColorsNode.empty()) {
                if (fadeColorsNode.isList()) {
                    holder = holder.withFadeColors(fadeColorsNode.childrenList()
                            .stream()
                            .map(this::deserializeColor)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                } else {
                    var color = deserializeColor(fadeColorsNode);
                    if (color != null) {
                        holder = holder.withFadeColors(List.of(color));
                    }
                }
            }
            return holder;
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable FireworkEffect obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(TYPE_KEY).set(obj.type().location().asString());
        node.node(FLICKER_KEY).set(obj.flicker());
        node.node(TRAIL_KEY).set(obj.trail());
        for (var color : obj.colors()) {
            node.node(COLORS_KEY).appendListNode().set(Color.class, color);
        }
        for (var color : obj.fadeColors()) {
            node.node(FADE_COLORS_KEY).appendListNode().set(Color.class, color);
        }
    }

    protected @Nullable Color deserializeColor(@NotNull ConfigurationNode colorNode) {
        try {
            return colorNode.get(Color.class);
        } catch (SerializationException ex) {
            return Color.WHITE;
        }
    }
}
