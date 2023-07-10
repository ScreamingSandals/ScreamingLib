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

package org.screamingsandals.lib.impl.adventure.spectator.sound;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.OptionalLong;

public class AdventureSoundStart extends BasicWrapper<Sound> implements SoundStart {
    public AdventureSoundStart(@NotNull Sound wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation soundKey() {
        return ResourceLocation.of(wrappedObject.name().namespace(), wrappedObject.name().value());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public @NotNull SoundStart withSoundKey(@NotNull ResourceLocation soundKey) {
        return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.path()), wrappedObject.source(), wrappedObject.volume(), wrappedObject.pitch()));
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
        if (AdventureFeature.SOUND_SEED.isSupported()) {
            var seed = wrappedObject.seed();
            return seed.isPresent() ? seed.getAsLong() : null;
        } else {
            // Only Adventure 4.12.0+
            return null;
        }
    }

    @Override
    public @NotNull SoundStart withSeed(@Nullable Long seed) {
        if (AdventureFeature.SOUND_SEED.isSupported()) {
            return new AdventureSoundStart(Sound.sound(wrappedObject).seed(seed == null ? OptionalLong.empty() : OptionalLong.of(seed)).build());
        } // Only Adventure 4.12.0+
        return this;
    }

    @Override
    public SoundStart.@NotNull Builder toBuilder() {
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

        private @Nullable ResourceLocation soundKey;
        private @NotNull SoundSource source = MASTER;
        private float volume = 1;
        private float pitch = 1;
        private @Nullable Long seed;

        @Override
        public @NotNull Builder soundKey(@NotNull ResourceLocation key) {
            if ("minecraft".equals(key.namespace())) {
                this.soundKey = ResourceLocation.of("minecraft", AdventureBackend.getSoundKeyNormalizer().apply(key.path()));
            } else {
                this.soundKey = key;
            }
            return this;
        }

        @Override
        public @NotNull Builder soundKey(@NotNull String key) {
            return soundKey(ResourceLocation.of(key));
        }

        @SuppressWarnings("PatternValidation")
        @Override
        public @NotNull SoundStart build() {
            Preconditions.checkNotNull(soundKey, "Sound key must be present!");
            if (AdventureFeature.SOUND_SEED.isSupported()) {
                // Adventure 4.12.0+
                return new AdventureSoundStart(
                        Sound.sound()
                                .type(Key.key(soundKey.namespace(), soundKey.path()))
                                .source(source.as(Sound.Source.class))
                                .volume(volume)
                                .pitch(pitch)
                                .seed(seed == null ? OptionalLong.empty() : OptionalLong.of(seed))
                                .build()
                );
            } else {
                // Adventure <= 4.11.0
                return new AdventureSoundStart(Sound.sound(Key.key(soundKey.namespace(), soundKey.path()), source.as(Sound.Source.class), volume, pitch));
            }
        }
    }
}
