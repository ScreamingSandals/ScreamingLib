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
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.ProtoEnchantment;
import org.screamingsandals.lib.utils.ProtoWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EnchantmentHolder extends ComparableWrapper, ProtoWrapper<ProtoEnchantment> {

    String platformName();

    int level();

    @Contract(value = "_ -> new", pure = true)
    EnchantmentHolder withLevel(int level);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(Object... objects);

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    default boolean isType(Object object) {
        return isSameType(object);
    }

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    default boolean isType(Object... objects) {
        return isSameType(objects);
    }

    /**
     * Use fluent variant
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    /**
     * Use fluent variant
     */
    @Deprecated(forRemoval = true)
    default int getLevel() {
        return level();
    }

    /**
     * Inconsistent naming
     */
    @Deprecated(forRemoval = true)
    default EnchantmentHolder newLevel(int level) {
        return withLevel(level);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    static EnchantmentHolder of(Object enchantment) {
        return ofOptional(enchantment).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    static Optional<EnchantmentHolder> ofOptional(Object enchantment) {
        if (enchantment instanceof EnchantmentHolder) {
            return Optional.of((EnchantmentHolder) enchantment);
        }
        return EnchantmentMapping.resolve(enchantment);
    }

    static List<EnchantmentHolder> all() {
        return EnchantmentMapping.getValues();
    }

    @Override
    default ProtoEnchantment asProto() {
        return ProtoEnchantment.newBuilder()
                .setPlatformName(platformName())
                .setLevel(level())
                .build();
    }
}
