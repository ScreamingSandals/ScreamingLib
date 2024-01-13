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

package org.screamingsandals.lib.gameevent;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.gameevent.GameEventRegistry;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;

@LimitedVersionSupport("Bukkit >= 1.17")
public interface GameEvent extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();


    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_EVENT) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_EVENT) @Nullable Object @NotNull... objects);

    static @NotNull GameEvent of(@MinecraftType(MinecraftType.Type.GAME_EVENT) @NotNull Object gameEvent) {
        var result = ofNullable(gameEvent);
        Preconditions.checkNotNullIllegal(result, "Could not find game event: " + gameEvent);
        return result;
    }

    @Contract("null -> null")
    static @Nullable GameEvent ofNullable(@MinecraftType(MinecraftType.Type.GAME_EVENT) @Nullable Object gameEvent) {
        if (gameEvent instanceof GameEvent) {
            return (GameEvent) gameEvent;
        }
        return GameEventRegistry.getInstance().resolveMapping(gameEvent);
    }

    static @NotNull RegistryItemStream<@NotNull GameEvent> all() {
        return GameEventRegistry.getInstance().getRegistryItemStream();
    }
}
