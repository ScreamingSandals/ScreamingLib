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
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface SoundStop extends Wrapper, RawValueHolder {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static SoundStop all() {
        return builder().build(); // TODO: constant
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static SoundStop named(NamespacedMappingKey soundKey) {
        return builder().soundKey(soundKey).build();
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static SoundStop sourced(SoundSource source) {
        return builder().source(source).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static SoundStop namedSourced(NamespacedMappingKey soundKey, SoundSource source) {
        return builder().soundKey(soundKey).source(source).build();
    }

    @Contract(value = "-> new", pure = true)
    @NotNull
    static SoundStop.Builder builder() {
        return Spectator.getBackend().soundStop();
    }

    @Nullable
    NamespacedMappingKey soundKey();

    @Nullable
    SoundSource source();

    @Contract(pure = true)
    @NotNull
    SoundStop withSoundKey(@Nullable NamespacedMappingKey soundKey);

    @Contract(pure = true)
    @NotNull
    SoundStop withSource(@Nullable SoundSource source);

    @Contract(value = "-> new", pure = true)
    @NotNull
    SoundStop.Builder toBuilder();

    interface Builder {
        @NotNull
        @Contract("_ -> this")
        Builder soundKey(@Nullable NamespacedMappingKey key);

        @NotNull
        @Contract("_ -> this")
        Builder source(@Nullable SoundSource source);

        @NotNull
        @Contract(value = "-> new", pure = true)
        SoundStop build();
    }
}