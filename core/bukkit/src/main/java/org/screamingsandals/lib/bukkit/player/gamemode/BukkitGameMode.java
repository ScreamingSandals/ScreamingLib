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

package org.screamingsandals.lib.bukkit.player.gamemode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.gamemode.GameMode;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitGameMode extends BasicWrapper<org.bukkit.GameMode> implements GameMode {
    public BukkitGameMode(@NotNull org.bukkit.GameMode wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int id() {
        return wrappedObject.getValue();
    }

    @Override
    public boolean is(@Nullable Object gameMode) {
        if (gameMode instanceof org.bukkit.GameMode || gameMode instanceof GameMode) {
            return equals(gameMode);
        }
        return equals(GameMode.ofNullable(gameMode));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... gameModes) {
        return Arrays.stream(gameModes).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.name());
    }
}
