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

package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitItemTypeMapper extends ItemTypeMapper {
    public BukkitItemTypeMapper() {
        if (Server.isVersion(1, 13)) {
            itemTypeConverter
                    .registerP2W(Material.class, BukkitItemTypeHolder::new)
                    .registerP2W(ItemStack.class, stack -> new BukkitItemTypeHolder(stack.getType()));


            Arrays.stream(Material.values())
                    .filter(t -> !t.name().startsWith("LEGACY"))
                    .filter(Material::isItem)
                    .forEach(material -> {
                        var holder = new BukkitItemTypeHolder(material);
                        var namespaced = material.getKey();
                        /* In case this is a hybrid server and it actually works correctly (unlike Mohist), we should not assume everything is in minecraft namespace */
                        mapping.put(NamespacedMappingKey.of(namespaced.getNamespace(), namespaced.getKey()), holder);
                        values.add(holder);
                    });
        } else {
            itemTypeConverter
                    .registerP2W(Material.class, BukkitItemTypeLegacyHolder::new)
                    .registerP2W(ItemStack.class, stack -> new BukkitItemTypeLegacyHolder(stack.getType(), stack.getDurability()));

            Arrays.stream(Material.values())
                    .forEach(material -> {
                        var holder = new BukkitItemTypeLegacyHolder(material);
                        /* In legacy we are not able to determine the namespace :( but hybrid servers require java 8 for 1.12.2 and less, so we can't run on them anyway */
                        mapping.put(NamespacedMappingKey.of(material.name()), holder);
                        values.add(holder);
                    });
        }
    }
}
