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

package org.screamingsandals.lib.bukkit.world;

import org.bukkit.Bukkit;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;
import org.screamingsandals.lib.world.Worlds;

@Service
public class BukkitLocations extends Locations {
    public BukkitLocations() {
        converter.registerW2P(org.bukkit.Location.class, holder -> {
            final var world = Bukkit.getWorld(holder.getWorld().getUuid());
            if (world == null) {
                return null;
            }
            return new org.bukkit.Location(world, holder.getX(), holder.getY(), holder.getZ(), holder.getYaw(), holder.getPitch());
        }).registerP2W(org.bukkit.Location.class, location ->
                new Location(location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch(), Worlds.wrapWorld(location.getWorld())));
    }
}
