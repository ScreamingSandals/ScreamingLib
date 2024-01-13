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
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.particle.*;

@UtilityClass
public class BukkitParticleConverter {
    public Object convertParticleData(@NotNull ParticleData data) {
        if (data instanceof Block) {
            if (BukkitFeature.FLATTENING.isSupported()) {
                return ((Block) data).as(BlockData.class);
            } else {
                return ((Block) data).as(MaterialData.class);
            }
        } else if (data instanceof ItemType) {
            return ((ItemType) data).as(org.bukkit.inventory.ItemStack.class);
        } else if (data instanceof ItemStack) {
            return ((ItemStack) data).as(org.bukkit.inventory.ItemStack.class);
        } else if (data instanceof DustOptions) {
            return new Particle.DustOptions(getBukkitColor(((DustOptions) data).color()), ((DustOptions) data).size());
        } else if (data instanceof DustTransition) {
            return new Particle.DustTransition(
                    getBukkitColor(((DustTransition) data).fromColor()),
                    getBukkitColor(((DustTransition) data).toColor()),
                    ((DustTransition) data).size()
            );
        } else if (data instanceof FloatData) {
            return ((FloatData) data).get();
        } else if (data instanceof IntegerData) {
            return ((IntegerData) data).get();
        } else if (data instanceof Vibration) {
            var origin = ((Vibration) data).origin();
            var dest = ((Vibration) data).destination();
            //noinspection removal
            return new org.bukkit.Vibration(
                    origin != null ? origin.as(Location.class) : new Location(null, 0, 0, 0), // useless since Spigot 1.19, but Spigot is fukin sh*t and needs non-null value
                    dest instanceof org.screamingsandals.lib.entity.Entity ?
                            new org.bukkit.Vibration.Destination.EntityDestination(dest.as(Entity.class))
                            : new org.bukkit.Vibration.Destination.BlockDestination(dest.as(Location.class)),
                    ((Vibration) data).arrivalTime()
            );
        }
        return null;
    }

    public Color getBukkitColor(org.screamingsandals.lib.spectator.Color rgb) {
        return Color.fromRGB(rgb.red(), rgb.green(), rgb.blue());
    }
}
