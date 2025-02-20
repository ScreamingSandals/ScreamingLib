/*
 * Copyright 2025 ScreamingSandals
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
import org.screamingsandals.lib.impl.bukkit.compat.v1_21_3.TargetColorCompat;
import org.screamingsandals.lib.impl.bukkit.utils.ColorUtils;
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
            return new Particle.DustOptions(ColorUtils.getBukkitColor(((DustOptions) data).color()), ((DustOptions) data).size());
        } else if (data instanceof DustTransition) {
            return new Particle.DustTransition(
                    ColorUtils.getBukkitColor(((DustTransition) data).fromColor()),
                    ColorUtils.getBukkitColor(((DustTransition) data).toColor()),
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
        } else if (data instanceof ParticleColor) {
            return ColorUtils.getBukkitColor(((ParticleColor) data).color());
        } else if (data instanceof Trail) {
            if (BukkitFeature.TRAIL_PARTICLE_API.isSupported()) {
                return new Particle.Trail(
                        ((Trail) data).location().as(Location.class),
                        ColorUtils.getBukkitColor(((Trail) data).color()),
                        ((Trail) data).duration()
                );
            } else if (BukkitFeature.TARGET_COLOR_PARTICLE_API.isSupported()) {
                return TargetColorCompat.convertTargetColor((Trail) data);
            }
        }
        return null;
    }
}
