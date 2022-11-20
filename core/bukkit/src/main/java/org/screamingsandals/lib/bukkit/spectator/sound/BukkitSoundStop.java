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
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Data
@Accessors(fluent = true)
public class BukkitSoundStop implements SoundStop {
    @Nullable
    private final NamespacedMappingKey soundKey;
    @Nullable
    private final SoundSource source;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for stopping custom sounds, just methods");
    }

    @Override
    public Object raw() {
        return null;
    }

    @Override
    @NotNull
    public SoundStop withSoundKey(@Nullable NamespacedMappingKey soundKey) {
        return new BukkitSoundStop(soundKey, source);
    }

    @Override
    @NotNull
    public SoundStop withSource(@Nullable SoundSource source) {
        return new BukkitSoundStop(soundKey, source);
    }

    @Override
    @NotNull
    public SoundStop.Builder toBuilder() {
        return new BukkitSoundStartBuilder(soundKey, source);
    }

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BukkitSoundStartBuilder implements Builder {
        private NamespacedMappingKey soundKey;
        private SoundSource source;

        @Override
        @NotNull
        public SoundStop build() {
            return new BukkitSoundStop(soundKey, source);
        }
    }
}
