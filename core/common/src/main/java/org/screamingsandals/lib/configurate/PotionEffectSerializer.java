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
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class PotionEffectSerializer implements TypeSerializer<PotionEffect> {
    private static final @NotNull String EFFECT_KEY = "effect";
    private static final @NotNull String DURATION_KEY = "duration";
    private static final @NotNull String AMPLIFIER_KEY = "amplifier";
    private static final @NotNull String AMBIENT_KEY = "ambient";
    private static final @NotNull String PARTICLES_KEY = "particles";
    private static final @NotNull String ICON_KEY = "icon";

    public static final @NotNull PotionEffectSerializer INSTANCE = new PotionEffectSerializer();

    @Override
    public @NotNull PotionEffect deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (!node.isMap()) {
                return PotionEffect.of(node.getString());
            }

            var effectNode = node.node(EFFECT_KEY);
            var durationNode = node.node(DURATION_KEY);
            var amplifierNode = node.node(AMPLIFIER_KEY);
            var ambientNode = node.node(AMBIENT_KEY);
            var particlesNode = node.node(PARTICLES_KEY);
            var iconNode = node.node(ICON_KEY);

            var holder = PotionEffect.of(effectNode.getString());
            return holder
                    .withDuration(durationNode.getInt(holder.duration()))
                    .withAmplifier(amplifierNode.getInt(holder.amplifier()))
                    .withAmbient(ambientNode.getBoolean(holder.ambient()))
                    .withParticles(particlesNode.getBoolean(node.node("has-particles").getBoolean(holder.particles()))) // older bw shop support (bukkit serializable compatibility)
                    .withIcon(iconNode.getBoolean(node.node("has-icon").getBoolean(holder.icon()))); // older bw shop support (bukkit serializable compatibility)
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable PotionEffect obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(EFFECT_KEY).set(obj.type().location().asString());
        node.node(DURATION_KEY).set(obj.duration());
        node.node(AMPLIFIER_KEY).set(obj.amplifier());
        node.node(AMBIENT_KEY).set(obj.ambient());
        node.node(PARTICLES_KEY).set(obj.particles());
        node.node(ICON_KEY).set(obj.icon());
    }
}
