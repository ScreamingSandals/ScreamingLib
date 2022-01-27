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

public interface BossBar extends Wrapper, RawValueHolder {
    @NotNull
    @Contract(value = "-> new", pure = true)
    static BossBar.Builder builder() {
        return Spectator.getBackend().bossBar();
    }

    @NotNull
    @Contract(value = "-> new", pure = true)
    static BossBar bossBar() {
        return builder().build();
    }

    @NotNull
    Component title();

    @NotNull
    @Contract("_ -> this")
    BossBar title(@NotNull Component title);

    float progress();

    @NotNull
    @Contract("_ -> this")
    BossBar progress(float progress);

    @NotNull
    @Unmodifiable
    List<BossBarFlag> flags();

    @NotNull
    @Contract("_ -> this")
    BossBar flags(@NotNull List<BossBarFlag> flags);

    @NotNull
    BossBarColor color();

    @NotNull
    @Contract("_ -> this")
    BossBar color(@NotNull BossBarColor color);

    @NotNull
    BossBarDivision division();

    @NotNull
    @Contract("_ -> this")
    BossBar division(@NotNull BossBarDivision division);

    @NotNull
    @Contract("_ -> new")
    RegisteredListener addListener(@NotNull BossBarListener listener);

    void removeListener(@NotNull RegisteredListener listener);

    interface Builder {
        @NotNull
        @Contract("_ -> this")
        Builder title(@NotNull Component title);

        @NotNull
        @Contract("_ -> this")
        Builder progress(float progress);

        @NotNull
        @Contract("_ -> this")
        Builder color(@NotNull BossBarColor color);

        @NotNull
        @Contract("_ -> this")
        Builder division(@NotNull BossBarDivision division);

        @NotNull
        @Contract("_ -> this")
        Builder flags(@NotNull Collection<BossBarFlag> flags);

        @NotNull
        @Contract("_ -> this")
        Builder flags(@NotNull BossBarFlag... flags);

        @NotNull
        @Contract("_ -> this")
        Builder listener(@NotNull BossBarListener listener);

        @NotNull
        @Contract(value = "-> new", pure = true)
        BossBar build();
    }
}
