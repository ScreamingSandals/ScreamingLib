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
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class SoundStartSerializer implements TypeSerializer<SoundStart> {
    public static final @NotNull SoundStartSerializer INSTANCE = new SoundStartSerializer();

    private static final @NotNull String NAME_KEY = "name";
    private static final @NotNull String SOURCE_KEY = "source";
    private static final @NotNull String PITCH_KEY = "pitch";
    private static final @NotNull String VOLUME_KEY = "volume";

    @Override
    public @NotNull SoundStart deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            if (node.isMap()) {
                final var name = ResourceLocation.of(node.node(NAME_KEY).getString());
                final var source = SoundSource.soundSource(node.node(SOURCE_KEY).getString("master").toLowerCase(Locale.ROOT));
                final var pitch = node.node(PITCH_KEY).getFloat(1f);
                final var volume = node.node(VOLUME_KEY).getFloat(1f);

                return SoundStart.builder()
                        .soundKey(name)
                        .source(source)
                        .pitch(pitch)
                        .volume(volume)
                        .build();
            } else {
                return SoundStart.builder()
                        .soundKey(ResourceLocation.of(node.getString()))
                        .source(SoundSource.soundSource("master"))
                        .volume(1)
                        .pitch(1)
                        .build();
            }
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable SoundStart obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(NAME_KEY).set(obj.soundKey().asString());
        node.node(SOURCE_KEY).set(obj.source().name());
        node.node(VOLUME_KEY).set(obj.volume());
        node.node(PITCH_KEY).set(obj.pitch());
    }
}
