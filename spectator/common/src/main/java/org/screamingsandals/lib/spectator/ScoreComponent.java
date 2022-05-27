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
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface ScoreComponent extends Component {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static ScoreComponent.Builder builder() {
        return Spectator.getBackend().score();
    }

    @NotNull
    String name();

    @Contract(pure = true)
    @NotNull
    ScoreComponent withName(@NotNull String name);

    @NotNull
    String objective();

    @Contract(pure = true)
    @NotNull
    ScoreComponent withObjective(@NotNull String objective);

    @LimitedVersionSupport("< 1.16.5")
    @Deprecated
    @Nullable
    String value();

    @Contract(pure = true)
    @NotNull
    ScoreComponent withValue(@Nullable String value);

    @Contract(value = "-> new", pure = true)
    @NotNull
    ScoreComponent.Builder toBuilder();

    interface Builder extends Component.Builder<Builder, ScoreComponent> {
        @Contract("_ -> this")
        @NotNull
        Builder name(@NotNull String name);

        @Contract("_ -> this")
        @NotNull
        Builder objective(@NotNull String objective);

        @Contract("_ -> this")
        @NotNull
        @LimitedVersionSupport("< 1.16.5")
        @Deprecated
        Builder value(@Nullable String value);
    }
}