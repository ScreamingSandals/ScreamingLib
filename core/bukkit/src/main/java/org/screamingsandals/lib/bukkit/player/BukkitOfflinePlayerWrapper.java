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

package org.screamingsandals.lib.bukkit.player;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.OfflinePlayerWrapper;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Objects;
import java.util.UUID;

public class BukkitOfflinePlayerWrapper extends BasicWrapper<OfflinePlayer> implements OfflinePlayerWrapper {
    public BukkitOfflinePlayerWrapper(@NotNull OfflinePlayer wrappedObject) {
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
    public @Nullable LocationHolder getBedLocation() {
        return LocationMapper.resolve(wrappedObject.getBedSpawnLocation());
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
    public void setOp(boolean op) {
        wrappedObject.setOp(op);
    }

    @Override
    public boolean isOnline() {
        return wrappedObject.isOnline();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == PlayerWrapper.class) {
            return (T) Objects.requireNonNull(PlayerMapper.getPlayer(wrappedObject.getUniqueId()));
        }

        return super.as(type);
    }
}
