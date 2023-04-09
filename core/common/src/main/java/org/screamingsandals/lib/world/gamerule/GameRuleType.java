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

package org.screamingsandals.lib.world.gamerule;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface GameRuleType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_RULE_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.GAME_RULE_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull GameRuleType of(@MinecraftType(MinecraftType.Type.GAME_RULE_TYPE) @NotNull Object gameRule) {
        var result = ofNullable(gameRule);
        Preconditions.checkNotNullIllegal(result, "Could not find game rule: " + gameRule);
        return result;
    }

    @Contract("null -> null")
    static @Nullable GameRuleType ofNullable(@MinecraftType(MinecraftType.Type.GAME_RULE_TYPE) @Nullable Object gameRule) {
        if (gameRule instanceof GameRuleType) {
            return (GameRuleType) gameRule;
        }
        return GameRuleRegistry.getInstance().resolveMapping(gameRule);
    }

    static @NotNull RegistryItemStream<@NotNull GameRuleType> all() {
        return GameRuleRegistry.getInstance().getRegistryItemStream();
    }
}
