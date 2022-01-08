/*
 * Copyright 2022 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class ProxiedPlayerMapper {

    protected final BidirectionalConverter<ProxiedPlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<ProxiedSenderWrapper> senderConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<ServerWrapper> serverConverter = BidirectionalConverter.build();
    private static ProxiedPlayerMapper proxiedPlayerMapper = null;

    public static void init(@NotNull Supplier<ProxiedPlayerMapper> proxiedPlayerMapperSupplier) {
        if (proxiedPlayerMapper != null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper are already initialized.");
        }

        proxiedPlayerMapper = proxiedPlayerMapperSupplier.get();
    }

    public static <T> ProxiedPlayerWrapper wrapPlayer(T player) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.playerConverter.convert(player);
    }

    public static <T> ProxiedSenderWrapper wrapSender(T player) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.senderConverter.convert(player);
    }

    public static <T> ServerWrapper wrapServer(T server) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.serverConverter.convert(server);
    }

    public static <T> T convertPlayerWrapper(ProxiedPlayerWrapper player, Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.playerConverter.convert(player, type);
    }

    public static <T> T convertSenderWrapper(ProxiedSenderWrapper sender, Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.senderConverter.convert(sender, type);
    }

    public static <T> T convertServerWrapper(ServerWrapper server, Class<T> type) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.serverConverter.convert(server, type);
    }

    public static void sendMessage(ProxiedSenderWrapper playerWrapper, String message) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        proxiedPlayerMapper.sendMessage0(playerWrapper, message);
    }

    public abstract void sendMessage0(ProxiedSenderWrapper playerWrapper, String message);

    public static void switchServer(ProxiedPlayerWrapper playerWrapper, ServerWrapper server) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        proxiedPlayerMapper.switchServer0(playerWrapper, server);
    }

    public abstract void switchServer0(ProxiedPlayerWrapper playerWrapper, ServerWrapper server);

    public static Optional<ServerWrapper> getServer(String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServer0(name);
    }

    public abstract Optional<ServerWrapper> getServer0(String name);

    public static List<ServerWrapper> getServers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getServers0();
    }

    public abstract List<ServerWrapper> getServers0();

    public static Optional<ProxiedPlayerWrapper> getPlayer(String name) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(name);
    }

    public abstract Optional<ProxiedPlayerWrapper> getPlayer0(String name);

    public static Optional<ProxiedPlayerWrapper> getPlayer(UUID uuid) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayer0(uuid);
    }

    public abstract Optional<ProxiedPlayerWrapper> getPlayer0(UUID uuid);

    public static List<ProxiedPlayerWrapper> getPlayers() {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0();
    }

    public abstract List<ProxiedPlayerWrapper> getPlayers0();

    public static List<ProxiedPlayerWrapper> getPlayers(ServerWrapper serverWrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper aren't initialized yet.");
        }
        return proxiedPlayerMapper.getPlayers0(serverWrapper);
    }

    public abstract List<ProxiedPlayerWrapper> getPlayers0(ServerWrapper serverWrapper);

    public static boolean hasPermission(CommandSenderWrapper wrapper, Permission permission) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("ProxiedPlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.hasPermission0(wrapper, permission);
    }

    public abstract boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission);

    public static boolean isPermissionSet(CommandSenderWrapper wrapper, Permission permission) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.isPermissionSet0(wrapper, permission);
    }

    public abstract boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission);

    public static Locale getLocale(ProxiedSenderWrapper wrapper) {
        if (proxiedPlayerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return proxiedPlayerMapper.getLocale0(wrapper);
    }

    public abstract Locale getLocale0(ProxiedSenderWrapper wrapper);

    public static boolean isInitialized() {
        return proxiedPlayerMapper != null;
    }
}
