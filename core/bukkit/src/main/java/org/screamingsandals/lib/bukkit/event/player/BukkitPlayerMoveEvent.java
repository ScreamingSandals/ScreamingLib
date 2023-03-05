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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.world.BukkitWorld;
import org.screamingsandals.lib.event.player.PlayerMoveEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.world.Location;

@Accessors(fluent = true)
// In this file we are directly unwrapping and wrapping locations instead of using BidirectionalConvertor to save some time
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerMoveEvent implements PlayerMoveEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerMoveEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable org.bukkit.Location currentLocationBukkit;
    private @Nullable Location currentLocation;
    private @Nullable org.bukkit.Location newLocationBukkit;
    private @Nullable Location newLocation;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull Location currentLocation() { // Mutable in Bukkit
        if (event.getFrom() != currentLocationBukkit) {
            currentLocationBukkit = event.getFrom();
            currentLocation = new Location(
                    currentLocationBukkit.getX(),
                    currentLocationBukkit.getY(),
                    currentLocationBukkit.getZ(),
                    currentLocationBukkit.getYaw(),
                    currentLocationBukkit.getPitch(),
                    new BukkitWorld(currentLocationBukkit.getWorld())
            );
        }
        return currentLocation;
    }

    @Override
    public @NotNull Location newLocation() {
        if (event.getTo() != newLocationBukkit) {
            newLocationBukkit = event.getTo();
            newLocation = new Location(
                    newLocationBukkit.getX(),
                    newLocationBukkit.getY(),
                    newLocationBukkit.getZ(),
                    newLocationBukkit.getYaw(),
                    newLocationBukkit.getPitch(),
                    new BukkitWorld(newLocationBukkit.getWorld())
            );
        }
        return newLocation;
    }

    @Override
    public void newLocation(@NotNull Location newLocation) {
        event.setTo(new org.bukkit.Location(
                newLocation.getWorld().as(World.class), // World is BasicWrapper, so the unwrapping is faster task
                newLocation.getX(),
                newLocation.getY(),
                newLocation.getZ(),
                newLocation.getYaw(),
                newLocation.getPitch()
        ));
    }
}
