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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface PotionHolder extends ComparableWrapper {

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    static PotionHolder of(Object potion) {
        var result = ofNullable(potion);
        Preconditions.checkNotNullIllegal(result, "Could not find potion: " + potion);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Contract("null -> null")
    static @Nullable PotionHolder ofNullable(@Nullable Object potion) {
        if (potion instanceof PotionHolder) {
            return (PotionHolder) potion;
        }
        return PotionMapping.resolve(potion);
    }

    static @NotNull List<@NotNull PotionHolder> all() {
        return PotionMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    boolean is(Object... objects);
}
