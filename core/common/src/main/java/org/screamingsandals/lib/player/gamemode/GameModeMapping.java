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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.GameModeHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
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
public abstract class GameModeMapping extends AbstractTypeMapper<GameModeHolder> {
    private static GameModeMapping gameModeMapping;

    protected final BidirectionalConverter<GameModeHolder> gameModeConverter = BidirectionalConverter.<GameModeHolder>build()
            .registerP2W(GameModeHolder.class, g -> g)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return GameModeHolderSerializer.INSTANCE.deserialize(GameModeHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public GameModeMapping() {
        if (gameModeMapping != null) {
            throw new UnsupportedOperationException("GameModeMapping is already initialized!");
        }
        gameModeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.GAME_MODE)
    @OfMethodAlternative(value = GameModeHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable GameModeHolder resolve(@Nullable Object gameMode) {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }

        if (gameMode == null) {
            return null;
        }

        return gameModeMapping.gameModeConverter.convertOptional(gameMode).or(() -> gameModeMapping.resolveFromMapping(gameMode)).orElse(null);
    }

    @OfMethodAlternative(value = GameModeHolder.class, methodName = "all")
    public static @NotNull List<@NotNull GameModeHolder> getValues() {
        if (gameModeMapping == null) {
            throw new UnsupportedOperationException("GameModeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(gameModeMapping.values);
    }
}
