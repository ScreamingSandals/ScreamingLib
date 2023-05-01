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

package org.screamingsandals.lib.impl.spectator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Color;

@ApiStatus.Internal
public abstract class ColorLink implements Color {
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

    protected abstract @NotNull Color obtainColor();

    @RequiredArgsConstructor
    public static class Named extends ColorLink {
        private final @NotNull String name;
        private @Nullable Color cache;

        protected @NotNull Color obtainColor() {
            if (cache == null) {
                cache = Spectator.getBackend().named(name); // Color.named() looks in the list of links, we don't want the link to point to itself
            }
            assert cache != null && !(cache instanceof ColorLink);
            return cache;
        }
    }

    @RequiredArgsConstructor
    public static class RGB extends ColorLink {
        private final int red;
        private final int green;
        private final int blue;
        private @Nullable Color cache;

        protected @NotNull Color obtainColor() {
            if (cache == null) {
                cache = Color.rgb(red, green, blue);
            }
            return cache;
        }
    }
}
