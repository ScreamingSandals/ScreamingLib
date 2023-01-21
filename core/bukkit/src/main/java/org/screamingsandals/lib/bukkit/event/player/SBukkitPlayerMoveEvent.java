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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

@Accessors(fluent = true)
// In this file we are directly unwrapping and wrapping locations instead of using BidirectionalConvertor to save some time
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerMoveEvent implements SPlayerMoveEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull PlayerMoveEvent event;

    // Internal cache
    private @Nullable PlayerWrapper player;
    private @Nullable Location currentLocationBukkit;
    private @Nullable LocationHolder currentLocation;
    private @Nullable Location newLocationBukkit;
    private @Nullable LocationHolder newLocation;

    @Override
    public @NotNull PlayerWrapper player() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull LocationHolder currentLocation() { // Mutable in Bukkit
        if (event.getFrom() != currentLocationBukkit) {
            currentLocationBukkit = event.getFrom();
            currentLocation = new LocationHolder(
                    currentLocationBukkit.getX(),
                    currentLocationBukkit.getY(),
                    currentLocationBukkit.getZ(),
                    currentLocationBukkit.getYaw(),
                    currentLocationBukkit.getPitch(),
                    new BukkitWorldHolder(currentLocationBukkit.getWorld())
            );
        }
        return currentLocation;
    }

    @Override
    public @NotNull LocationHolder newLocation() {
        if (event.getTo() != newLocationBukkit) {
            newLocationBukkit = event.getTo();
            newLocation = new LocationHolder(
                    newLocationBukkit.getX(),
                    newLocationBukkit.getY(),
                    newLocationBukkit.getZ(),
                    newLocationBukkit.getYaw(),
                    newLocationBukkit.getPitch(),
                    new BukkitWorldHolder(newLocationBukkit.getWorld())
            );
        }
        return newLocation;
    }

    @Override
    public void newLocation(@NotNull LocationHolder newLocation) {
        event.setTo(new Location(
                newLocation.getWorld().as(World.class), // World is BasicWrapper, so the unwrapping is faster task
                newLocation.getX(),
                newLocation.getY(),
                newLocation.getZ(),
                newLocation.getYaw(),
                newLocation.getPitch()
        ));
    }
}
