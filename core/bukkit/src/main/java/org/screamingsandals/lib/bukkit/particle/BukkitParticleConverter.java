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

package org.screamingsandals.lib.bukkit.particle;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockType;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.particle.DustOptions;
import org.screamingsandals.lib.particle.DustTransition;
import org.screamingsandals.lib.particle.ParticleData;

@UtilityClass
public class BukkitParticleConverter {
    public Object convertParticleData(@NotNull ParticleData data) {
        if (data instanceof BlockType) {
            if (Version.isVersion(1, 13)) {
                return data.as(BlockData.class);
            } else {
                return data.as(MaterialData.class);
            }
        } else if (data instanceof ItemType) {
            return data.as(org.bukkit.inventory.ItemStack.class);
        } else if (data instanceof ItemStack) {
            return data.as(org.bukkit.inventory.ItemStack.class);
        } else if (data instanceof DustOptions) {
            return new Particle.DustOptions(getBukkitColor(((DustOptions) data).color()), ((DustOptions) data).size());
        } else if (data instanceof DustTransition) {
            return new Particle.DustTransition(
                    getBukkitColor(((DustTransition) data).fromColor()),
                    getBukkitColor(((DustTransition) data).toColor()),
                    ((DustTransition) data).size()
            );
        }
        return null;
    }

    public Color getBukkitColor(org.screamingsandals.lib.spectator.Color rgb) {
        return Color.fromRGB(rgb.red(), rgb.green(), rgb.blue());
    }
}
