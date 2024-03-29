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

package org.screamingsandals.lib.impl.bukkit.particle;

import lombok.experimental.UtilityClass;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.particle.ParticleData;

@UtilityClass
public class BukkitParticleConverter1_8 {
    public int @NotNull [] convertParticleData(@NotNull ParticleData data) {
        if (data instanceof Block) {
            var materialData = ((Block) data).as(MaterialData.class);
            return new int[] {materialData.getItemType().getId() + ((int)(materialData.getData()) << 12)};
        } else if (data instanceof ItemType || data instanceof ItemStack) {
            var stack = ((Wrapper) data).as(org.bukkit.inventory.ItemStack.class);
            return new int[] {stack.getType().getId(), stack.getDurability()};
        }
        return new int[0];
    }
}
