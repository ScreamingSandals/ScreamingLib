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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

// TODO: Support for AudienceComponentLike
public interface Book extends Wrapper, RawValueHolder {
    @NotNull
    @Contract(value = "-> new", pure = true)
    static Book.Builder builder() {
        return Spectator.getBackend().book();
    }

    @NotNull
    Component title();

    @Contract(pure = true)
    @NotNull
    Book withTitle(@NotNull Component title);

    @NotNull
    Component author();

    @Contract(pure = true)
    @NotNull
    Book withAuthor(@NotNull Component author);

    @Unmodifiable
    @NotNull
    List<Component> pages();

    @Contract(pure = true)
    @NotNull
    Book withPages(@NotNull List<Component> pages);

    @Contract(pure = true)
    @NotNull
    Book withPages(@NotNull Component... pages);

    @Contract(value = "-> new", pure = true)
    @NotNull
    Book.Builder toBuilder();

    interface Builder {
        @NotNull
        @Contract("_ -> this")
        Builder title(@NotNull Component title);

        @NotNull
        @Contract("_ -> this")
        Builder author(@NotNull Component author);

        @NotNull
        @Contract("_ -> this")
        Builder pages(@NotNull List<Component> pages);

        @NotNull
        @Contract("_ -> this")
        Builder pages(@NotNull Component... pages);

        @NotNull
        @Contract(value = "-> new", pure = true)
        Book build();
    }
}