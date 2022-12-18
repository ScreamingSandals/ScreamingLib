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

package org.screamingsandals.lib.world.gamerule;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface GameRuleHolder extends ComparableWrapper {

    String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    static @NotNull GameRuleHolder of(@NotNull Object gameRule) {
        var result = ofNullable(gameRule);
        Preconditions.checkNotNullIllegal(result, "Could not find game rule: " + gameRule);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    @Contract("null -> null")
    static @Nullable GameRuleHolder ofNullable(@Nullable Object gameRule) {
        if (gameRule instanceof GameRuleHolder) {
            return (GameRuleHolder) gameRule;
        }
        return GameRuleMapping.resolve(gameRule);
    }

    static @NotNull List<@NotNull GameRuleHolder> all() {
        return GameRuleMapping.getValues();
    }
}
