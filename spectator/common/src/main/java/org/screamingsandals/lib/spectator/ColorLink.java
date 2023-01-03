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

package org.screamingsandals.lib.spectator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@ApiStatus.Internal
class ColorLink implements Color {
    private final @NotNull String name;
    private @Nullable Color cache;

    private @NotNull Color obtainColor() {
        if (cache == null) {
            cache = Color.named(name);
        }
        assert cache != null;
        return cache;
    }

    @Override
    public int red() {
        return obtainColor().red();
    }

    @Override
    public int green() {
        return obtainColor().green();
    }

    @Override
    public int blue() {
        return obtainColor().blue();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return obtainColor().as(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obtainColor().equals(obj);
    }

    @Override
    public int hashCode() {
        return obtainColor().hashCode();
    }

    @Override
    public @NotNull String toString() {
        return obtainColor().toString();
    }

    @Override
    public @NotNull Object raw() {
        return obtainColor().raw();
    }
}
