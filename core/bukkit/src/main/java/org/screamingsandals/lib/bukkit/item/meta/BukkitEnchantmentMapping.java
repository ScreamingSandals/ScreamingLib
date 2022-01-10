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
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEnchantmentMapping extends EnchantmentMapping {
    public BukkitEnchantmentMapping() {
        enchantmentConverter
                .registerP2W(Enchantment.class, BukkitEnchantmentHolder::new);

        Arrays.stream(Enchantment.values()).forEach(enchantment -> {
            var holder = new BukkitEnchantmentHolder(enchantment);
            mapping.put(NamespacedMappingKey.of(enchantment.getName()), holder);
            values.add(holder);
        });
    }
}
