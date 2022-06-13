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

package org.screamingsandals.lib.spectator.event.hover;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface ItemContent extends Content, ItemContentLike {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static ItemContent.Builder builder() {
        return Spectator.getBackend().itemContent();
    }

    @NotNull
    NamespacedMappingKey id();

    @Contract(pure = true)
    @NotNull
    ItemContent withId(@NotNull NamespacedMappingKey id);

    int count();

    @Contract(pure = true)
    @NotNull
    ItemContent withCount(int count);

    // TODO: NBT api
    @Nullable
    String tag();

    @Contract(pure = true)
    @NotNull
    ItemContent withTag(@Nullable String tag);

    @Contract(value = "-> new", pure = true)
    @NotNull
    ItemContent.Builder toBuilder();

    @Override
    @NotNull
    default ItemContent asItemContent() {
        return this;
    }

    @Override
    @NotNull
    default Content asContent() {
        return this;
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull
        Builder id(NamespacedMappingKey id);

        @Contract("_ -> this")
        @NotNull
        Builder count(int count);

        // TODO: NBT api
        @Contract("_ -> this")
        @NotNull
        Builder tag(@Nullable String tag);

        @Contract(value = "-> new", pure = true)
        @NotNull
        ItemContent build();
    }
}
