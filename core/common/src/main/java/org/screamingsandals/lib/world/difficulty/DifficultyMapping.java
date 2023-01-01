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

package org.screamingsandals.lib.world.difficulty;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.DifficultyHolderSerializer;
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
public abstract class DifficultyMapping extends AbstractTypeMapper<DifficultyHolder> {
    private static DifficultyMapping difficultyMapping;

    protected final BidirectionalConverter<DifficultyHolder> difficultyConverter = BidirectionalConverter.<DifficultyHolder>build()
            .registerP2W(DifficultyHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return DifficultyHolderSerializer.INSTANCE.deserialize(DifficultyHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public DifficultyMapping() {
        if (difficultyMapping != null) {
            throw new UnsupportedOperationException("DifficultyMapping is already initialized!");
        }
        difficultyMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    @OfMethodAlternative(value = DifficultyHolder.class, methodName = "ofNullable")
    @Contract(value = "null -> null")
    public static @Nullable DifficultyHolder resolve(Object difficulty) {
        if (difficultyMapping == null) {
            throw new UnsupportedOperationException("DifficultyMapping is not initialized yet.");
        }

        if (difficulty == null) {
            return null;
        }

        return difficultyMapping.difficultyConverter.convertOptional(difficulty).or(() -> difficultyMapping.resolveFromMapping(difficulty)).orElse(null);
    }

    @OfMethodAlternative(value = DifficultyHolder.class, methodName = "all")
    public static @NotNull List<@NotNull DifficultyHolder> getValues() {
        if (difficultyMapping == null) {
            throw new UnsupportedOperationException("DifficultyMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(difficultyMapping.values);
    }
}
