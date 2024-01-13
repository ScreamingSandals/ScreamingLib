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

package org.screamingsandals.lib.impl.bukkit.spectator.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitServer;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;

@Data
@Accessors(fluent = true)
public class BukkitSoundStart implements SoundStart {
    private final @NotNull ResourceLocation soundKey;
    private final @NotNull SoundSource source;
    private final float volume;
    private final float pitch;
    private final @Nullable Long seed;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for custom sounds, just methods");
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException("Bukkit doesn't have any class for custom sounds, just methods");
    }

    @Override
    public @NotNull SoundStart withSoundKey(@NotNull ResourceLocation soundKey) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    public @NotNull SoundStart withSource(@NotNull SoundSource source) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    public @NotNull SoundStart withVolume(float volume) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    public @NotNull SoundStart withPitch(float pitch) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    public @Nullable Long seed() {
        return seed;
    }

    @Override
    public @NotNull SoundStart withSeed(@Nullable Long seed) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    public SoundStart.@NotNull Builder toBuilder() {
        return new BukkitSoundStartBuilder(soundKey, source, volume, pitch, seed);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class BukkitSoundStartBuilder implements SoundStart.Builder {
        private @Nullable ResourceLocation soundKey;
        private @NotNull SoundSource source = SoundSource.soundSource("master");
        private float volume = 1;
        private float pitch = 1;
        private @Nullable Long seed;

        @Override
        public @NotNull Builder soundKey(@NotNull ResourceLocation key) {
            if ("minecraft".equals(key.namespace())) {
                this.soundKey = ResourceLocation.of("minecraft", BukkitServer.UNSAFE_normalizeSoundKey0(key.path()));
            } else {
                this.soundKey = key;
            }
            return this;
        }

        @Override
        public @NotNull Builder soundKey(@NotNull String key) {
            return soundKey(ResourceLocation.of(key));
        }

        @Override
        public @NotNull SoundStart build() {
            Preconditions.checkNotNull(soundKey, "Sound key cannot be null");
            return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
        }
    }
}
