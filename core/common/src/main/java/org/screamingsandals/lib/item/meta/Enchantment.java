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
import org.screamingsandals.lib.impl.item.meta.EnchantmentRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface Enchantment extends RegistryItem {

    @NotNull String platformName();

    int level();

    @Contract(value = "_ -> new", pure = true)
    @NotNull Enchantment withLevel(int level);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @Nullable Object @NotNull... objects);

    boolean isSameType(@MinecraftType(MinecraftType.Type.ENCHANTMENT_TYPE) @Nullable Object object);

    boolean isSameType(@MinecraftType(MinecraftType.Type.ENCHANTMENT_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull Enchantment of(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @NotNull Object enchantment) {
        var result = ofNullable(enchantment);
        Preconditions.checkNotNullIllegal(result, "Could not find enchantment: " + enchantment);
        return result;
    }

    @Contract("null -> null")
    static @Nullable Enchantment ofNullable(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @Nullable Object enchantment) {
        if (enchantment instanceof Enchantment) {
            return (Enchantment) enchantment;
        }
        return EnchantmentRegistry.getInstance().resolveMapping(enchantment);
    }

    static @NotNull RegistryItemStream<@NotNull Enchantment> all() {
        return EnchantmentRegistry.getInstance().getRegistryItemStream();
    }
}
