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
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.utils.ResourceLocation;

@Data
@Accessors(fluent = true)
public class BukkitSoundStop implements SoundStop {
    private final @Nullable ResourceLocation soundKey;
    private final @Nullable SoundSource source;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for stopping custom sounds, just methods");
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException("Bukkit doesn't have any class for stopping custom sounds, just methods");
    }

    @Override
    public @NotNull SoundStop withSoundKey(@Nullable ResourceLocation soundKey) {
        return new BukkitSoundStop(soundKey, source);
    }

    @Override
    public @NotNull SoundStop withSource(@Nullable SoundSource source) {
        return new BukkitSoundStop(soundKey, source);
    }

    @Override
    public SoundStop.@NotNull Builder toBuilder() {
        return new BukkitSoundStartBuilder(soundKey, source);
    }

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BukkitSoundStartBuilder implements Builder {
        private @Nullable ResourceLocation soundKey;
        private @Nullable SoundSource source;

        @Override
        public @NotNull Builder soundKey(@Nullable ResourceLocation key) {
            if (key == null) {
                soundKey = null;
                return this;
            }

            if ("minecraft".equals(key.namespace())) {
                this.soundKey = ResourceLocation.of("minecraft", BukkitServer.UNSAFE_normalizeSoundKey0(key.path()));
            } else {
                this.soundKey = key;
            }
            return this;
        }

        @Override
        public @NotNull Builder soundKey(@Nullable String key) {
            if (key == null) {
                soundKey = null;
                return this;
            }

            return soundKey(ResourceLocation.of(key));
        }

        @Override
        public @NotNull SoundStop build() {
            return new BukkitSoundStop(soundKey, source);
        }
    }
}
