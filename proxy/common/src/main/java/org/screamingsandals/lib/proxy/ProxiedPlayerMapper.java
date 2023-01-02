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

package org.screamingsandals.lib.proxy;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@AbstractService
public abstract class ProxiedPlayerMapper {

    protected final @NotNull BidirectionalConverter<ProxiedPlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final @NotNull BidirectionalConverter<ProxiedSenderWrapper> senderConverter = BidirectionalConverter.build();
    protected final @NotNull BidirectionalConverter<ServerWrapper> serverConverter = BidirectionalConverter.build();
    private static @Nullable ProxiedPlayerMapper proxiedPlayerMapper;

    @ApiStatus.Internal
    public ProxiedPlayerMapper() {
        if (proxiedPlayerMapper != null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper are already initialized.");
        }

        proxiedPlayerMapper = this;
    }

    public static <T> @NotNull ProxiedPlayerWrapper wrapPlayer(@NotNull T player) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.playerConverter.convert(player);
    }

    public static <T> @NotNull ProxiedSenderWrapper wrapSender(@NotNull T player) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.senderConverter.convert(player);
    }

    public static <T> @NotNull ServerWrapper wrapServer(@NotNull T server) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.serverConverter.convert(server);
    }

    public static <T> @NotNull T convertPlayerWrapper(@NotNull ProxiedPlayerWrapper player, @NotNull Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.playerConverter.convert(player, type);
    }

    public static <T> @NotNull T convertSenderWrapper(@NotNull ProxiedSenderWrapper sender, @NotNull Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.senderConverter.convert(sender, type);
    }

    public static <T> @NotNull T convertServerWrapper(@NotNull ServerWrapper server, @NotNull Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.serverConverter.convert(server, type);
    }

    public static void sendMessage(@NotNull ProxiedSenderWrapper playerWrapper, @NotNull String message) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        proxiedPlayerMapper.sendMessage0(playerWrapper, message);
    }

    public abstract void sendMessage0(@NotNull ProxiedSenderWrapper playerWrapper, @NotNull String message);

    public static void switchServer(@NotNull ProxiedPlayerWrapper playerWrapper, @NotNull ServerWrapper server) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        proxiedPlayerMapper.switchServer0(playerWrapper, server);
    }

    public abstract void switchServer0(@NotNull ProxiedPlayerWrapper playerWrapper, @NotNull ServerWrapper server);

    public static @Nullable ServerWrapper getServer(@NotNull String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServer0(name);
    }

    public abstract @Nullable ServerWrapper getServer0(@NotNull String name);

    public static @NotNull List<@NotNull ServerWrapper> getServers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServers0();
    }

    public abstract @NotNull List<@NotNull ServerWrapper> getServers0();

    public static @Nullable ProxiedPlayerWrapper getPlayer(@NotNull String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(name);
    }

    public abstract @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull String name);

    public static @Nullable ProxiedPlayerWrapper getPlayer(@NotNull UUID uuid) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(uuid);
    }

    public abstract @Nullable ProxiedPlayerWrapper getPlayer0(@NotNull UUID uuid);

    public static @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0();
    }

    public abstract @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers0();

    public static @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers(@NotNull ServerWrapper serverWrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0(serverWrapper);
    }

    public abstract @NotNull List<@NotNull ProxiedPlayerWrapper> getPlayers0(@NotNull ServerWrapper serverWrapper);

    public static boolean hasPermission(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.hasPermission0(wrapper, permission);
    }

    public abstract boolean hasPermission0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission);

    public static boolean isPermissionSet(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.isPermissionSet0(wrapper, permission);
    }

    public abstract boolean isPermissionSet0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission);

    public static @NotNull Locale getLocale(@NotNull ProxiedSenderWrapper wrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.getLocale0(wrapper);
    }

    public abstract @NotNull Locale getLocale0(@NotNull ProxiedSenderWrapper wrapper);

    public static @NotNull Adapter adapter(@NotNull ProxiedSenderWrapper wrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.adapter0(wrapper);
    }

    protected abstract @NotNull Adapter adapter0(@NotNull ProxiedSenderWrapper wrapper);
}
