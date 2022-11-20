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

package org.screamingsandals.lib.bukkit.spectator.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Data
@Accessors(fluent = true)
public class BukkitSoundStart implements SoundStart {
    @NotNull
    private final NamespacedMappingKey soundKey;
    @NotNull
    private final SoundSource source;
    private final float volume;
    private final float pitch;
    @Nullable
    private final Long seed;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for custom sounds, just methods");
    }

    @Override
    public Object raw() {
        return null;
    }

    @Override
    @NotNull
    public SoundStart withSoundKey(@NotNull NamespacedMappingKey soundKey) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    @NotNull
    public SoundStart withSource(@NotNull SoundSource source) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    @NotNull
    public SoundStart withVolume(float volume) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    @NotNull
    public SoundStart withPitch(float pitch) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    @Nullable
    public Long seed() {
        return seed;
    }

    @Override
    @NotNull
    public SoundStart withSeed(@Nullable Long seed) {
        return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
    }

    @Override
    @NotNull
    public SoundStart.Builder toBuilder() {
        return new BukkitSoundStartBuilder(soundKey, source, volume, pitch, seed);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class BukkitSoundStartBuilder implements SoundStart.Builder {
        private NamespacedMappingKey soundKey;
        private SoundSource source = SoundSource.soundSource("master");
        private float volume = 1;
        private float pitch = 1;
        private Long seed = null;

        @Override
        @NotNull
        public SoundStart build() {
            Preconditions.checkNotNull(soundKey, "Sound key cannot be null");
            return new BukkitSoundStart(soundKey, source, volume, pitch, seed);
        }
    }
}
