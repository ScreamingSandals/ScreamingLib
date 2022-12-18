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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.GameRuleHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class GameRuleMapping extends AbstractTypeMapper<GameRuleHolder> {
    private static GameRuleMapping gameRuleMapping;

    protected final BidirectionalConverter<GameRuleHolder> gameRuleConverter = BidirectionalConverter.<GameRuleHolder>build()
            .registerP2W(GameRuleHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return GameRuleHolderSerializer.INSTANCE.deserialize(GameRuleHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public GameRuleMapping() {
        Preconditions.checkArgument(gameRuleMapping == null, "GameRuleMapping is already initialized!");
        gameRuleMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_RULE)
    @OfMethodAlternative(value = GameRuleHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable GameRuleHolder resolve(@Nullable Object gameRule) {
        Preconditions.checkNotNull(gameRuleMapping, "GameRuleMapping is not initialized yet!");

        if (gameRule == null) {
            return null;
        }

        return gameRuleMapping.gameRuleConverter.convertOptional(gameRule).or(() -> gameRuleMapping.resolveFromMapping(gameRule)).orElse(null);
    }

    @OfMethodAlternative(value = GameRuleHolder.class, methodName = "all")
    public static @NotNull List<@NotNull GameRuleHolder> getValues() {
        Preconditions.checkNotNull(gameRuleMapping, "GameRuleMapping is not initialized yet!");
        return Collections.unmodifiableList(gameRuleMapping.values);
    }
}
