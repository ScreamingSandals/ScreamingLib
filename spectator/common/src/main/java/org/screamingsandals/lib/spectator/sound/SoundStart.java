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

package org.screamingsandals.lib.spectator.sound;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface SoundStart extends Wrapper, RawValueHolder {
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull SoundStart sound(@NotNull NamespacedMappingKey soundKey, @NotNull SoundSource soundSource, float volume, float pitch) {
        return builder()
                .soundKey(soundKey)
                .source(soundSource)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @CustomAutocompletion(CustomAutocompletion.Type.SOUND)
    static @NotNull SoundStart minecraftSound(@NotNull String soundKey, @NotNull SoundSource soundSource, float volume, float pitch) {
        return builder()
                .soundKey(soundKey)
                .source(soundSource)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    @Contract(value = "-> new", pure = true)
    static SoundStart.@NotNull Builder builder() {
        return Spectator.getBackend().soundStart();
    }

    @NotNull NamespacedMappingKey soundKey();

    @Contract(pure = true)
    @NotNull SoundStart withSoundKey(@NotNull NamespacedMappingKey soundKey);

    @NotNull SoundSource source();

    @Contract(pure = true)
    @NotNull SoundStart withSource(@NotNull SoundSource source);

    float volume();

    @Contract(pure = true)
    @NotNull SoundStart withVolume(float volume);

    float pitch();

    @Contract(pure = true)
    @NotNull SoundStart withPitch(float pitch);

    @LimitedVersionSupport(">= 1.19")
    @Nullable Long seed();

    @Contract(pure = true)
    @LimitedVersionSupport(">= 1.19")
    @NotNull SoundStart withSeed(@Nullable Long seed);

    @Contract(value = "-> new", pure = true)
    SoundStart.@NotNull Builder toBuilder();

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder soundKey(@NotNull NamespacedMappingKey key);

        @Contract("_ -> this")
        @CustomAutocompletion(CustomAutocompletion.Type.SOUND)
        default @NotNull Builder soundKey(@NotNull String key) {
            return soundKey(NamespacedMappingKey.of(key));
        }

        @Contract("_ -> this")
        @NotNull Builder source(@NotNull SoundSource source);

        @Contract("_ -> this")
        @NotNull Builder volume(float volume);

        @Contract("_ -> this")
        @NotNull Builder pitch(float pitch);

        @Contract("_ -> this")
        @LimitedVersionSupport(">= 1.19")
        @NotNull Builder seed(@Nullable Long seed);

        @Contract(value = "-> new", pure = true)
        @NotNull SoundStart build();
    }
}
