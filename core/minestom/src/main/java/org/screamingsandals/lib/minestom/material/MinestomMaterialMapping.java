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

package org.screamingsandals.lib.minestom.material;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomMaterialMapping extends MaterialMapping {

    public static void init() {
        MaterialMapping.init(MinestomMaterialMapping::new);
    }

    public MinestomMaterialMapping() {
        platform = Platform.JAVA_FLATTENING;

        materialConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .registerW2P(ItemStack.class, holder -> new ItemStack(Material.valueOf(holder.getPlatformName()), (byte) 1, holder.getDurability()))
                .registerW2P(Block.class, holder -> Block.valueOf(holder.getPlatformName()))
                .registerP2W(Material.class, material -> new MaterialHolder(material.name()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(stack.getMaterial().name()))
                .registerP2W(Block.class, block -> new MaterialHolder(block.getName()));

        Arrays.stream(Material.values()).forEach(material -> mapping.put(NamespacedMappingKey.of(material.getName()), new MaterialHolder(material.name())));
    }
}
