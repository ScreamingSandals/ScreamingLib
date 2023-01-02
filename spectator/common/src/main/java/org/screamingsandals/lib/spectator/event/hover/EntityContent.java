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

package org.screamingsandals.lib.spectator.event.hover;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public interface EntityContent extends Content, EntityContentLike {
    @Contract(value = "-> new", pure = true)
    static @NotNull EntityContent.Builder builder() {
        return Spectator.getBackend().entityContent();
    }

    @NotNull
    UUID id();

    @Contract(pure = true)
    @NotNull
    EntityContent withId(@NotNull UUID id);

    @NotNull
    NamespacedMappingKey type();

    @Contract(pure = true)
    @NotNull
    EntityContent withType(@NotNull NamespacedMappingKey type);

    @Nullable
    Component name();

    @Contract(pure = true)
    @NotNull
    EntityContent withType(@Nullable Component name);

    @Contract(value = "-> new", pure = true)
    @NotNull
    EntityContent.Builder toBuilder();

    @Override
    default @NotNull EntityContent asEntityContent() {
        return this;
    }

    @Override
    default @NotNull Content asContent() {
        return this;
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull
        Builder id(@NotNull UUID id);

        @Contract("_ -> this")
        @NotNull
        Builder type(@NotNull NamespacedMappingKey type);

        @Contract("_ -> this")
        @NotNull
        Builder name(@Nullable Component name);

        @Contract(value = "-> new", pure = true)
        @NotNull
        EntityContent build();
    }
}
