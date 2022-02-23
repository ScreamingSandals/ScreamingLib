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

package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomItemTypeMapper extends ItemTypeMapper {
    public MinestomItemTypeMapper() {
        itemTypeConverter
                .registerP2W(Material.class, MinestomItemTypeHolder::new)
                .registerP2W(ItemStack.class, stack -> new MinestomItemTypeHolder(stack.getMaterial()));

        Material.values().stream()
                .filter(material -> !material.isBlock())
                .forEach(material -> {
                    final var holder = new MinestomItemTypeHolder(material);
                    mapping.put(NamespacedMappingKey.of(material.name()), holder);
                    values.add(holder);
                });
    }
}
