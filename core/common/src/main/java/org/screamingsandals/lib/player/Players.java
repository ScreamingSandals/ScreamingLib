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

package org.screamingsandals.lib.player;

import io.netty.channel.Channel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.ProvidedService;

import java.util.*;

@ProvidedService
public abstract class Players {
    protected final @NotNull BidirectionalConverter<OfflinePlayer> offlinePlayerConverter = BidirectionalConverter.build();
    protected final @NotNull BidirectionalConverter<Player> specialPlayerConverter = BidirectionalConverter.build();
    private static @Nullable Players players;

    @ApiStatus.Internal
    public Players() {
        if (players != null) {
            throw new UnsupportedOperationException("Players is already initialized.");
        }
        players = this;
    }

    public static <T> @NotNull Player wrapPlayer(@NotNull T player) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        try {
            var sender = players.wrapSender0(player);

            if (sender instanceof Player) {
                return (Player) sender;
            }
        } catch (UnsupportedOperationException ignored) {}

        return players.specialPlayerConverter.convert(player);
    }

    public static <T> OfflinePlayer wrapOfflinePlayer(@NotNull T player) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.offlinePlayerConverter.convert(player);
    }

    public static <T> @NotNull CommandSender wrapSender(@NotNull T sender) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.wrapSender0(sender);
    }

    public static <T> T convertOfflinePlayer(@NotNull OfflinePlayer player, @NotNull Class<T> type) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.offlinePlayerConverter.convert(player, type);
    }

    @Contract("null -> null")
    public static @Nullable Player getPlayer(@Nullable String name) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return players.getPlayer0(name);
    }

    public abstract @Nullable Player getPlayer0(@NotNull String name);

    @Contract("null -> null")
    public static @Nullable Player getPlayer(@Nullable UUID uuid) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        if (uuid == null) {
            return null;
        }
        return players.getPlayer0(uuid);
    }

    public abstract @Nullable Player getPlayer0(@NotNull UUID uuid);

    @Contract("null -> null")
    public static @Nullable Player getPlayerExact(@Nullable String name) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return players.getPlayerExact0(name);
    }

    public static boolean hasPermission(@NotNull CommandSender wrapper, @NotNull Permission permission) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.hasPermission0(wrapper, permission);
    }

    public static boolean isPermissionSet(@NotNull CommandSender wrapper, @NotNull Permission permission) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.isPermissionSet0(wrapper, permission);
    }

    public static @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        return players.getOfflinePlayer0(uuid);
    }

    /**
     * This method may involve a blocking web request to get the UUID for the given name.
     *
     * @param name Name of the player
     * @return the offline player or null if not found
     * @deprecated see {@link Players#getOfflinePlayer(UUID)}
     */
    @Deprecated
    @Contract("null -> null")
    public static @Nullable OfflinePlayer getOfflinePlayer(@Nullable String name) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return players.getOfflinePlayer0(name);
    }

    public static @NotNull BidirectionalConverter<Player> UNSAFE_getPlayerConverter() {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }
        
        return players.specialPlayerConverter;
    }

    // TODO: ChannelWrapper (Minestom doesn't have Netty-backed channels).
    public static @Nullable Channel getNettyChannel(@NotNull Player player) {
        if (players == null) {
            throw new UnsupportedOperationException("Players isn't initialized yet.");
        }

        return players.getNettyChannel0(player);
    }

    // abstract methods for implementations

    protected abstract @Nullable Channel getNettyChannel0(@NotNull Player player);

    protected abstract <T> @NotNull CommandSender wrapSender0(@NotNull T sender);

    public abstract boolean hasPermission0(@NotNull CommandSender wrapper, @NotNull Permission permission);

    public abstract boolean isPermissionSet0(@NotNull CommandSender wrapper, @NotNull Permission permission);

    public abstract @NotNull OfflinePlayer getOfflinePlayer0(@NotNull UUID uuid);

    public abstract @Nullable OfflinePlayer getOfflinePlayer0(@NotNull String name);

    public abstract @Nullable Player getPlayerExact0(@NotNull String name);
}
