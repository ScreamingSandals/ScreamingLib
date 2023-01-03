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

package org.screamingsandals.lib.spectator.bossbar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Collection;
import java.util.List;

// TODO: Support for AudienceComponentLike
public interface BossBar extends Wrapper, RawValueHolder {
    @Contract(value = "-> new", pure = true)
    static BossBar.@NotNull Builder builder() {
        return Spectator.getBackend().bossBar();
    }

    @Contract(value = "-> new", pure = true)
    static @NotNull BossBar bossBar() {
        return builder().build();
    }

    @NotNull Component title();

    @Contract("_ -> this")
    @NotNull BossBar title(@NotNull Component title);

    float progress();

    @Contract("_ -> this")
    @NotNull BossBar progress(float progress);

    @NotNull @Unmodifiable List<@NotNull BossBarFlag> flags();

    @Contract("_ -> this")
    @NotNull BossBar flags(@NotNull List<@NotNull BossBarFlag> flags);

    @NotNull BossBarColor color();

    @Contract("_ -> this")
    @NotNull BossBar color(@NotNull BossBarColor color);

    @NotNull BossBarDivision division();

    @Contract("_ -> this")
    @NotNull BossBar division(@NotNull BossBarDivision division);

    @Contract("_ -> new")
    @NotNull RegisteredListener addListener(@NotNull BossBarListener listener);

    void removeListener(@NotNull RegisteredListener listener);

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder title(@NotNull Component title);

        @Contract("_ -> this")
        @NotNull Builder progress(float progress);

        @Contract("_ -> this")
        @NotNull Builder color(@NotNull BossBarColor color);

        @Contract("_ -> this")
        @NotNull Builder division(@NotNull BossBarDivision division);

        @Contract("_ -> this")
        @NotNull Builder flags(@NotNull Collection<@NotNull BossBarFlag> flags);

        @Contract("_ -> this")
        @NotNull Builder flags(@NotNull BossBarFlag @NotNull... flags);

        @Contract("_ -> this")
        @NotNull Builder listener(@NotNull BossBarListener listener);

        @Contract(value = "-> new", pure = true)
        @NotNull BossBar build();
    }
}
