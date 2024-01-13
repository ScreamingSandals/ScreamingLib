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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.Collection;
import java.util.List;

public interface TranslatableComponent extends Component {
    @Contract(value = "-> new", pure = true)
    static TranslatableComponent.@NotNull Builder builder() {
        return Spectator.getBackend().translatable();
    }

    @NotNull String translate();

    @Contract(pure = true)
    @NotNull TranslatableComponent withTranslate(@NotNull String translate);

    @NotNull List<Component> args();

    @Contract(pure = true)
    @NotNull TranslatableComponent withArgs(@NotNull Component @NotNull...components);

    @Contract(pure = true)
    @NotNull TranslatableComponent withArgs(@NotNull Collection<Component> components);

    @LimitedVersionSupport(">= 1.19.4")
    @Nullable String fallback();

    @LimitedVersionSupport(">= 1.19.4")
    @Contract(pure = true)
    @NotNull TranslatableComponent withFallback(@Nullable String fallback);

    @Contract(value = "-> new", pure = true)
    TranslatableComponent.@NotNull Builder toBuilder();

    interface Builder extends Component.Builder<Builder, TranslatableComponent> {
        @Contract("_ -> this")
        @NotNull Builder translate(@NotNull String translate);

        @Contract("_ -> this")
        @NotNull Builder args(@NotNull Component @NotNull...components);

        @Contract("_ -> this")
        @NotNull Builder args(@NotNull Collection<Component> components);

        @LimitedVersionSupport(">= 1.19.4")
        @Contract("_ -> this")
        @NotNull Builder fallback(@Nullable String fallback);
    }
}
