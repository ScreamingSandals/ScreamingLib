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

package org.screamingsandals.lib.spectator.sound;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface SoundStart extends Wrapper, RawValueHolder {
    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static SoundStart sound(NamespacedMappingKey soundKey, SoundSource soundSource, float volume, float pitch) {
        return builder()
                .soundKey(soundKey)
                .source(soundSource)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    @NotNull
    @Contract(value = "-> new", pure = true)
    static SoundStart.Builder builder() {
        return Spectator.getBackend().soundStart();
    }

    @NotNull
    NamespacedMappingKey soundKey();

    @Contract(pure = true)
    @NotNull
    SoundStart withSoundKey(@NotNull NamespacedMappingKey soundKey);

    @NotNull
    SoundSource source();

    @Contract(pure = true)
    @NotNull
    SoundStart withSource(@NotNull SoundSource source);

    float volume();

    @Contract(pure = true)
    @NotNull
    SoundStart withVolume(float volume);

    float pitch();

    @Contract(pure = true)
    @NotNull
    SoundStart withPitch(float pitch);

    @LimitedVersionSupport(">= 1.19")
    @Nullable
    Long seed();

    @Contract(pure = true)
    @NotNull
    @LimitedVersionSupport(">= 1.19")
    SoundStart withSeed(@Nullable Long seed);

    @Contract(value = "-> new", pure = true)
    @NotNull
    SoundStart.Builder toBuilder();

    interface Builder {
        @NotNull
        @Contract("_ -> this")
        Builder soundKey(@NotNull NamespacedMappingKey key);

        @NotNull
        @Contract("_ -> this")
        Builder source(@NotNull SoundSource source);

        @NotNull
        @Contract("_ -> this")
        Builder volume(float volume);

        @NotNull
        @Contract("_ -> this")
        Builder pitch(float pitch);

        @NotNull
        @Contract("_ -> this")
        @LimitedVersionSupport(">= 1.19")
        Builder seed(@Nullable Long seed);

        @NotNull
        @Contract(value = "-> new", pure = true)
        SoundStart build();
    }
}
