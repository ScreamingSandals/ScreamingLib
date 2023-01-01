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

package org.screamingsandals.lib.player.gamemode;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface GameModeHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    int id();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    boolean is(Object gameMode);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    boolean is(Object... gameModes);

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    static @NotNull GameModeHolder of(@NotNull Object gameMode) {
        var result = ofNullable(gameMode);
        Preconditions.checkNotNullIllegal(result, "Could not find game mode: " + gameMode);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    @Contract("null -> null")
    static @Nullable GameModeHolder ofNullable(@Nullable Object gameMode) {
        if (gameMode instanceof GameModeHolder) {
            return (GameModeHolder) gameMode;
        }
        return GameModeMapping.resolve(gameMode);
    }

    static @NotNull List<@NotNull GameModeHolder> all() {
        return GameModeMapping.getValues();
    }
}
