/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.player.PlayerTeleportEvent;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.impl.world.Locations;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.world.Location;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerTeleportEvent implements PlayerTeleportEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerTeleportEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable org.bukkit.Location currentLocationBukkit;
    private @Nullable Location currentLocation;
    private @Nullable org.bukkit.Location newLocationBukkit;
    private @Nullable Location newLocation;
    private @Nullable TeleportCause teleportCause;

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
            currentLocation = Locations.wrapLocation(currentLocationBukkit);
        }
        return currentLocation;
    }

    @Override
    public @NotNull Location newLocation() {
        if (event.getTo() != newLocationBukkit) {
            newLocationBukkit = event.getTo();
            newLocation = Locations.wrapLocation(newLocationBukkit);
        }
        return newLocation;
    }

    @Override
    public void newLocation(@NotNull Location newLocation) {
        event.setTo(newLocation.as(org.bukkit.Location.class));
    }

    @Override
    public @NotNull TeleportCause cause() {
        if (teleportCause == null) {
            try {
                teleportCause = TeleportCause.valueOf(event().getCause().name());
            } catch (IllegalArgumentException ignored) {
                teleportCause = TeleportCause.UNKNOWN;
            }
        }
        return teleportCause;
    }
}
