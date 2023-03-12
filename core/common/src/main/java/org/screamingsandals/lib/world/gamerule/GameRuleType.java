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
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface GameRuleType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(@Nullable Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    static @NotNull GameRuleType of(@NotNull Object gameRule) {
        var result = ofNullable(gameRule);
        Preconditions.checkNotNullIllegal(result, "Could not find game rule: " + gameRule);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    @Contract("null -> null")
    static @Nullable GameRuleType ofNullable(@Nullable Object gameRule) {
        if (gameRule instanceof GameRuleType) {
            return (GameRuleType) gameRule;
        }
        return GameRuleRegistry.getInstance().resolveMapping(gameRule);
    }

    static @NotNull RegistryItemStream<@NotNull GameRuleType> all() {
        return GameRuleRegistry.getInstance().getRegistryItemStream();
    }
}
