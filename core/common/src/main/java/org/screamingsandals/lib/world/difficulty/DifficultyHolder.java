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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface DifficultyHolder extends ComparableWrapper, RawValueHolder {
    @NotNull String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    static @NotNull DifficultyHolder of(@NotNull Object difficulty) {
        var result = ofNullable(difficulty);
        Preconditions.checkNotNullIllegal(result, "Could not find difficulty: " + difficulty);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DIFFICULTY)
    @Contract("null -> null")
    static @Nullable DifficultyHolder ofNullable(@Nullable Object difficulty) {
        if (difficulty instanceof DifficultyHolder) {
            return (DifficultyHolder) difficulty;
        }
        return DifficultyMapping.resolve(difficulty);
    }

    static @Unmodifiable @NotNull List<@NotNull DifficultyHolder> all() {
        return DifficultyMapping.getValues();
    }
}
