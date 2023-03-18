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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

public interface EnchantmentHolder extends ComparableWrapper {

    @NotNull String platformName();

    int level();

    @Contract(value = "_ -> new", pure = true)
    @NotNull EnchantmentHolder withLevel(int level);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    static @NotNull EnchantmentHolder of(@NotNull Object enchantment) {
        var result = ofNullable(enchantment);
        Preconditions.checkNotNullIllegal(result, "Could not find enchantment: " + enchantment);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Contract("null -> null")
    static @Nullable EnchantmentHolder ofNullable(@Nullable Object enchantment) {
        if (enchantment instanceof EnchantmentHolder) {
            return (EnchantmentHolder) enchantment;
        }
        return EnchantmentMapping.resolve(enchantment);
    }

    static @Unmodifiable @NotNull List<@NotNull EnchantmentHolder> all() {
        return EnchantmentMapping.getValues();
    }
}
