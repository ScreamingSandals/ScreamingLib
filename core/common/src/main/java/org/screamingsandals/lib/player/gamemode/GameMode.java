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

package org.screamingsandals.lib.player.gamemode;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.impl.player.gamemode.GameModeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface GameMode extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    int id();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_MODE) @Nullable Object gameMode);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_MODE) @Nullable Object @NotNull... gameModes);

    static @NotNull GameMode of(@MinecraftType(MinecraftType.Type.GAME_MODE) @NotNull Object gameMode) {
        var result = ofNullable(gameMode);
        Preconditions.checkNotNullIllegal(result, "Could not find game mode: " + gameMode);
        return result;
    }

    @Contract("null -> null")
    static @Nullable GameMode ofNullable(@MinecraftType(MinecraftType.Type.GAME_MODE) @Nullable Object gameMode) {
        if (gameMode instanceof GameMode) {
            return (GameMode) gameMode;
        }
        return GameModeRegistry.getInstance().resolveMapping(gameMode);
    }

    static @NotNull RegistryItemStream<@NotNull GameMode> all() {
        return GameModeRegistry.getInstance().getRegistryItemStream();
    }
}
