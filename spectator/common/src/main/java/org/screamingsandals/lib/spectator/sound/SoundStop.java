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
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.key.ResourceLocation;

public interface SoundStop extends Wrapper, RawValueHolder {
    @Contract(value = "-> new", pure = true)
    static @NotNull SoundStop all() {
        return builder().build(); // TODO: constant
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SoundStop named(@Nullable ResourceLocation soundKey) {
        return builder().soundKey(soundKey).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SoundStop minecraftNamed(@MinecraftType(MinecraftType.Type.SOUND) @Nullable String soundKey) {
        return builder().soundKey(soundKey).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull SoundStop sourced(@Nullable SoundSource source) {
        return builder().source(source).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull SoundStop namedSourced(@Nullable ResourceLocation soundKey, @Nullable SoundSource source) {
        return builder().soundKey(soundKey).source(source).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull SoundStop minecraftNamedSourced(@MinecraftType(MinecraftType.Type.SOUND) @Nullable String soundKey, @Nullable SoundSource source) {
        return builder().soundKey(soundKey).source(source).build();
    }

    @Contract(value = "-> new", pure = true)
    static SoundStop.@NotNull Builder builder() {
        return Spectator.getBackend().soundStop();
    }

    @Nullable ResourceLocation soundKey();

    @Nullable SoundSource source();

    @Contract(pure = true)
    @NotNull SoundStop withSoundKey(@Nullable ResourceLocation soundKey);

    @Contract(pure = true)
    @NotNull SoundStop withSource(@Nullable SoundSource source);

    @Contract(value = "-> new", pure = true)
    SoundStop.@NotNull Builder toBuilder();

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder soundKey(@Nullable ResourceLocation key);

        @Contract("_ -> this")
        default @NotNull Builder soundKey(@MinecraftType(MinecraftType.Type.SOUND) @Nullable String key) {
            return soundKey(key != null ? ResourceLocation.of(key) : null);
        }

        @Contract("_ -> this")
        @NotNull Builder source(@Nullable SoundSource source);

        @Contract(value = "-> new", pure = true)
        @NotNull SoundStop build();
    }
}
