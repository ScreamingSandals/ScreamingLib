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

package org.screamingsandals.lib.impl.spectator.sound;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.sound.SoundSource;

@RequiredArgsConstructor
@ApiStatus.Internal
public final class SoundSourceLink implements SoundSource {
    private final @NotNull String name;
    private @Nullable SoundSource cache;

    private @NotNull SoundSource obtainSource() {
        if (cache == null) {
            cache = SoundSource.soundSource(name);
        }
        return cache;
    }

    @Override
    public @NotNull String name() {
        return obtainSource().name();
    }

    @Override
    public @NotNull Object raw() {
        return obtainSource().raw();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return obtainSource().as(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obtainSource().equals(obj);
    }

    @Override
    public int hashCode() {
        return obtainSource().hashCode();
    }

    @Override
    public String toString() {
        return obtainSource().toString();
    }
}
