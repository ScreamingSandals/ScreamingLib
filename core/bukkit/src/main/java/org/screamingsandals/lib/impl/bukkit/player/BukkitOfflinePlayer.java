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

package org.screamingsandals.lib.impl.bukkit.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.OfflinePlayer;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;

import java.util.Objects;
import java.util.UUID;

public class BukkitOfflinePlayer extends BasicWrapper<org.bukkit.OfflinePlayer> implements OfflinePlayer {
    public BukkitOfflinePlayer(@NotNull org.bukkit.OfflinePlayer wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public @Nullable String getLastName() {
        return wrappedObject.getName();
    }

    @Override
    public @Nullable Location getBedLocation() {
        return Locations.resolve(wrappedObject.getBedSpawnLocation());
    }

    @Override
    public long getFirstPlayed() {
        return wrappedObject.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return wrappedObject.getLastPlayed();
    }

    @Override
    public boolean isBanned() {
        return wrappedObject.isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return wrappedObject.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        wrappedObject.setWhitelisted(whitelisted);
    }

    @Override
    public boolean isOp() {
        return wrappedObject.isOp();
    }

    @Override
    public boolean isOnline() {
        return wrappedObject.isOnline();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Player.class) {
            return (T) Objects.requireNonNull(Players.getPlayer(wrappedObject.getUniqueId()));
        }

        return super.as(type);
    }
}
