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
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface ItemContent extends Content, ItemContentLike {
    @Contract(value = "-> new", pure = true)
    static ItemContent.@NotNull Builder builder() {
        return Spectator.getBackend().itemContent();
    }

    @NotNull NamespacedMappingKey id();

    @Contract(pure = true)
    @NotNull ItemContent withId(@NotNull NamespacedMappingKey id);

    int count();

    @Contract(pure = true)
    @NotNull ItemContent withCount(int count);

    @Nullable CompoundTag tag();

    @Contract(pure = true)
    @NotNull ItemContent withTag(@Nullable CompoundTag tag);

    @Contract(value = "-> new", pure = true)
    ItemContent.@NotNull Builder toBuilder();

    @Override
    default @NotNull ItemContent asItemContent() {
        return this;
    }

    @Override
    default @NotNull Content asContent() {
        return this;
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder id(@NotNull NamespacedMappingKey id);

        @Contract("_ -> this")
        @NotNull Builder count(int count);

        @Contract("_ -> this")
        @NotNull Builder tag(@Nullable CompoundTag tag);

        @Contract(value = "-> new", pure = true)
        @NotNull ItemContent build();
    }
}
