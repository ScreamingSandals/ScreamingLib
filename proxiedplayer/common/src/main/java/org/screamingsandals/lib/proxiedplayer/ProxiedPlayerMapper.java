package org.screamingsandals.lib.proxiedplayer;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class ProxiedPlayerMapper {

    protected final BidirectionalConverter<ProxiedPlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<ProxiedSenderWrapper> senderConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<ServerWrapper> serverConverter = BidirectionalConverter.build();
    private static ProxiedPlayerMapper proxiedPlayerUtils = null;

    public static void init(@NotNull Supplier<ProxiedPlayerMapper> proxiedPlayerUtilsSupplier) {
        if (proxiedPlayerUtils != null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils are already initialized.");
        }

        proxiedPlayerUtils = proxiedPlayerUtilsSupplier.get();
    }

    public static <T> ProxiedPlayerWrapper wrapPlayer(T player) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.playerConverter.convert(player);
    }

    public static <T> ProxiedSenderWrapper wrapSender(T player) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.senderConverter.convert(player);
    }

    public static <T> ServerWrapper wrapServer(T server) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.serverConverter.convert(server);
    }

    public static <T> T convertPlayerWrapper(ProxiedPlayerWrapper player, Class<T> type) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.playerConverter.convert(player, type);
    }

    public static <T> T convertSenderWrapper(ProxiedSenderWrapper sender, Class<T> type) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.senderConverter.convert(sender, type);
    }

    public static <T> T convertServerWrapper(ServerWrapper server, Class<T> type) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.serverConverter.convert(server, type);
    }

    public static void sendMessage(ProxiedSenderWrapper playerWrapper, String message) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        proxiedPlayerUtils.sendMessage0(playerWrapper, message);
    }

    public abstract void sendMessage0(ProxiedSenderWrapper playerWrapper, String message);

    public static void switchServer(ProxiedPlayerWrapper playerWrapper, ServerWrapper server) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        proxiedPlayerUtils.switchServer0(playerWrapper, server);
    }

    public abstract void switchServer0(ProxiedPlayerWrapper playerWrapper, ServerWrapper server);

    public static Optional<ServerWrapper> getServer(String name) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getServer0(name);
    }

    public abstract Optional<ServerWrapper> getServer0(String name);

    public static List<ServerWrapper> getServers() {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getServers0();
    }

    public abstract List<ServerWrapper> getServers0();

    public static Optional<ProxiedPlayerWrapper> getPlayer(String name) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getPlayer0(name);
    }

    public abstract Optional<ProxiedPlayerWrapper> getPlayer0(String name);

    public static Optional<ProxiedPlayerWrapper> getPlayer(UUID uuid) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getPlayer0(uuid);
    }

    public abstract Optional<ProxiedPlayerWrapper> getPlayer0(UUID uuid);

    public static List<ProxiedPlayerWrapper> getPlayers() {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getPlayers0();
    }

    public abstract List<ProxiedPlayerWrapper> getPlayers0();

    public static List<ProxiedPlayerWrapper> getPlayers(ServerWrapper serverWrapper) {
        if (proxiedPlayerUtils == null) {
            throw new UnsupportedOperationException("ProxiedPlayerUtils aren't initialized yet.");
        }
        return proxiedPlayerUtils.getPlayers0(serverWrapper);
    }

    public abstract List<ProxiedPlayerWrapper> getPlayers0(ServerWrapper serverWrapper);

    public static boolean isInitialized() {
        return proxiedPlayerUtils != null;
    }
}
