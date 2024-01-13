/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.item.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.util.Arrays;

public class BukkitEnchantment extends BasicWrapper<Pair<org.bukkit.enchantments.Enchantment, Integer>> implements Enchantment {

    public BukkitEnchantment(@NotNull org.bukkit.enchantments.Enchantment enchantment) {
        this(Pair.of(enchantment, 1));
    }

    public BukkitEnchantment(@NotNull Pair<org.bukkit.enchantments.@NotNull Enchantment, @NotNull Integer> wrappedObject) {
        super(wrappedObject);
    }

    public BukkitEnchantment(org.bukkit.enchantments.@NotNull Enchantment enchantment, int level) {
        this(Pair.of(enchantment, level));
    }

    @Override
    public @NotNull EnchantmentType type() {
        return EnchantmentType.of(wrappedObject.first());
    }

    @Override
    public int level() {
        return wrappedObject.second();
    }

    @Override
    public @NotNull Enchantment withLevel(int level) {
        return new BukkitEnchantment(Pair.of(wrappedObject.first(), level));
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.enchantments.Enchantment) {
            return wrappedObject.equals(Pair.of(object, 1));
        }
        if (object instanceof Enchantment) {
            return equals(object);
        }
        return equals(Enchantment.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == org.bukkit.enchantments.Enchantment.class) {
            return (T) wrappedObject.first();
        }
        return super.as(type);
    }
}
