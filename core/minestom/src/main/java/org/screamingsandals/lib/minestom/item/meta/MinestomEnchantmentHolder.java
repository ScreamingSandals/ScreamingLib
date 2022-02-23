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

package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.item.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.util.Arrays;

public class MinestomEnchantmentHolder extends BasicWrapper<Pair<Enchantment, Short>> implements EnchantmentHolder {
    protected MinestomEnchantmentHolder(Enchantment enchantment) {
        this(Pair.of(enchantment, (short) 1));
    }

    public MinestomEnchantmentHolder(Pair<Enchantment, Short> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.first().name();
    }

    @Override
    public int level() {
        return wrappedObject.second();
    }

    @Override
    public EnchantmentHolder withLevel(int level) {
        return new MinestomEnchantmentHolder(Pair.of(wrappedObject.first(), (short) level));
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Enchantment) {
            return wrappedObject.equals(Pair.of(object, (short) 1));
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
        } else if (object instanceof MinestomEnchantmentHolder) {
            return ((MinestomEnchantmentHolder) object).wrappedObject.first().equals(wrappedObject.first());
        }
        return platformName().equals(EnchantmentHolder.ofOptional(object).map(EnchantmentHolder::platformName).orElse(null));
    }

    @Override
    public boolean isSameType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == Enchantment.class) {
            return (T) wrappedObject.first();
        }
        return super.as(type);
    }
}
