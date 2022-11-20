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

package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.util.Arrays;

public class BukkitEnchantmentHolder extends BasicWrapper<Pair<Enchantment, Integer>> implements EnchantmentHolder {

    public BukkitEnchantmentHolder(Enchantment enchantment) {
        this(Pair.of(enchantment, 1));
    }

    public BukkitEnchantmentHolder(Pair<Enchantment, Integer> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.first().getName();
    }

    @Override
    public int level() {
        return wrappedObject.second();
    }

    @Override
    public EnchantmentHolder withLevel(int level) {
        return new BukkitEnchantmentHolder(Pair.of(wrappedObject.first(), level));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Enchantment) {
            return wrappedObject.equals(Pair.of(object, 1));
        }
        if (object instanceof EnchantmentHolder) {
            return equals(object);
        }
        return equals(EnchantmentHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(Object object) {
        if (object instanceof Enchantment) {
            return wrappedObject.first().equals(object);
        } else if (object instanceof BukkitEnchantmentHolder) {
            return ((BukkitEnchantmentHolder) object).wrappedObject.first().equals(wrappedObject.first());
        }
        return platformName().equals(EnchantmentHolder.ofOptional(object).map(EnchantmentHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Enchantment.class) {
            return (T) wrappedObject.first();
        }
        return super.as(type);
    }
}
