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

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface GameRuleHolder extends ComparableWrapper {

    String platformName();

    /**
     * Use fluent form!!!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    static GameRuleHolder of(Object gameRule) {
        return ofOptional(gameRule).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    static Optional<GameRuleHolder> ofOptional(Object gameRule) {
        if (gameRule instanceof GameRuleHolder) {
            return Optional.of((GameRuleHolder) gameRule);
        }
        return GameRuleMapping.resolve(gameRule);
    }

    static List<GameRuleHolder> all() {
        return GameRuleMapping.getValues();
    }
}
