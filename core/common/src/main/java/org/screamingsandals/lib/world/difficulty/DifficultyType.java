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

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface DifficultyType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    static @NotNull DifficultyType of(@NotNull Object difficulty) {
        var result = ofNullable(difficulty);
        Preconditions.checkNotNullIllegal(result, "Could not find difficulty: " + difficulty);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    @Contract("null -> null")
    static @Nullable DifficultyType ofNullable(@Nullable Object difficulty) {
        if (difficulty instanceof DifficultyType) {
            return (DifficultyType) difficulty;
        }
        return DifficultyRegistry.getInstance().resolveMapping(difficulty);
    }

    static @NotNull RegistryItemStream<@NotNull DifficultyType> all() {
        return DifficultyRegistry.getInstance().getRegistryItemStream();
    }
}
