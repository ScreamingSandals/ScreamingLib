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

package org.screamingsandals.lib.adventure.spectator.sound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.OptionalLong;

public class AdventureSoundStart extends BasicWrapper<Sound> implements SoundStart {
    public AdventureSoundStart(@NotNull Sound wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull NamespacedMappingKey soundKey() {
        return NamespacedMappingKey.of(wrappedObject.name().namespace(), wrappedObject.name().value());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public @NotNull SoundStart withSoundKey(@NotNull NamespacedMappingKey soundKey) {
        return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.value()), wrappedObject.source(), wrappedObject.volume(), wrappedObject.pitch()));
    }

    @Override
    public @NotNull SoundSource source() {
        return new AdventureSoundSource(wrappedObject.source());
    }

    @Override
    public @NotNull SoundStart withSource(@NotNull SoundSource source) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), source.as(Sound.Source.class), wrappedObject.volume(), wrappedObject.pitch()));
    }

    @Override
    public float volume() {
        return wrappedObject.volume();
    }

    @Override
    public @NotNull SoundStart withVolume(float volume) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), wrappedObject.source(), volume, wrappedObject.pitch()));
    }

    @Override
    public float pitch() {
        return wrappedObject.pitch();
    }

    @Override
    public @NotNull SoundStart withPitch(float pitch) {
        return new AdventureSoundStart(Sound.sound(wrappedObject.name(), wrappedObject.source(), wrappedObject.volume(), pitch));
    }

    @Override
    public @Nullable Long seed() {
        try {
            var seed = wrappedObject.seed();
            return seed.isPresent() ? seed.getAsLong() : null;
        } catch (Throwable ignored) {
            // Only Adventure 4.12.0+
        }
        return null;
    }

    @Override
    public @NotNull SoundStart withSeed(@Nullable Long seed) {
        try {
            return new AdventureSoundStart(Sound.sound(wrappedObject).seed(seed == null ? OptionalLong.empty() : OptionalLong.of(seed)).build());
        } catch (Throwable ignored) {
            // Only Adventure 4.12.0+
        }
        return this;
    }

    @Override
    public @NotNull SoundStart.Builder toBuilder() {
        return new AdventureSoundStartBuilder(
                soundKey(),
                source(),
                volume(),
                pitch(),
                seed()
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureSoundStartBuilder implements SoundStart.Builder {
        private static final @NotNull SoundSource MASTER = SoundSource.soundSource("master");

        private @Nullable NamespacedMappingKey soundKey;
        private @NotNull SoundSource source = MASTER;
        private float volume = 1;
        private float pitch = 1;
        private @Nullable Long seed;

        @SuppressWarnings("PatternValidation")
        @Override
        public @NotNull SoundStart build() {
            Preconditions.checkNotNull(soundKey, "Sound key must be present!");
            try {
                // Adventure 4.12.0+
                return new AdventureSoundStart(
                        Sound.sound()
                                .type(Key.key(soundKey.namespace(), soundKey.value()))
                                .source(source.as(Sound.Source.class))
                                .volume(volume)
                                .pitch(pitch)
                                .seed(seed == null ? OptionalLong.empty() : OptionalLong.of(seed))
                                .build()
                );
            } catch (Throwable ignored) {
                // Adventure <= 4.11.0
                return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.value()), source.as(Sound.Source.class), volume, pitch));
            }
        }
    }
}
