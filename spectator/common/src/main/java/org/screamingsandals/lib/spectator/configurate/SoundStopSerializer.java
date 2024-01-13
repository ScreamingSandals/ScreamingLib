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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SoundStopSerializer implements TypeSerializer<SoundStop> {
    public static final @NotNull SoundStopSerializer INSTANCE = new SoundStopSerializer();

    private static final @NotNull String SOUND_KEY = "sound";
    private static final @NotNull String SOURCE_KEY = "source";

    @Override
    public @NotNull SoundStop deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (node.empty()) {
                return SoundStop.builder().build();
            }
            final var sound = node.node(SOUND_KEY).getString();
            final var source = node.node(SOURCE_KEY).getString();
            if (sound != null) {
                if (source == null) {
                    return SoundStop.builder().soundKey(ResourceLocation.of(sound)).build();
                } else {
                    return SoundStop.builder().soundKey(ResourceLocation.of(sound)).source(SoundSource.soundSource(source)).build();
                }
            } else if (source != null) {
                return SoundStop.builder().source(SoundSource.soundSource(source)).build();
            } else {
                return SoundStop.builder().build();
            }
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable SoundStop obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }
        node.node(SOUND_KEY).set(obj.soundKey() != null ? obj.soundKey().asString() : null);
        node.node(SOURCE_KEY).set(obj.source() != null ? obj.source().name() : null);
    }
}
