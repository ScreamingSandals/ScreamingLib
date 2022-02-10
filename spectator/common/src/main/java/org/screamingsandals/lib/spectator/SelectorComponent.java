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
import org.jetbrains.annotations.Nullable;

public interface SelectorComponent extends SeparableComponent {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static SelectorComponent.Builder builder() {
        return Spectator.getBackend().selector();
    }

    @NotNull
    String pattern();

    @Contract(pure = true)
    @NotNull
    SelectorComponent withPattern(String pattern);

    @Contract(value = "-> new", pure = true)
    @NotNull
    SelectorComponent.Builder toBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    @NotNull
    SelectorComponent withSeparator(@Nullable Component separator);

    interface Builder extends SeparableComponent.Builder<Builder, SelectorComponent> {
        @Contract("_ -> this")
        @NotNull
        Builder pattern(@NotNull String pattern);
    }
}
