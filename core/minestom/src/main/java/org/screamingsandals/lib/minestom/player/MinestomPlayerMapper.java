package org.screamingsandals.lib.minestom.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.screamingsandals.lib.minestom.entity.MinestomEntityPlayer;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.UUID;

@Service
public class MinestomPlayerMapper extends PlayerMapper {
    public MinestomPlayerMapper() {
        offlinePlayerConverter
                .registerP2W(Player.class, player -> getPlayer0(player.getUuid()).orElse(null))
                .registerW2P(Player.class, offlinePlayerWrapper -> MinecraftServer.getConnectionManager().getPlayer(offlinePlayerWrapper.getUuid()));
        handConverter
                .registerW2P(Player.Hand.class, wrapper -> Player.Hand.valueOf(wrapper.name()))
                .registerP2W(Player.Hand.class, hand -> PlayerWrapper.Hand.valueOf(hand.name()));
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return Optional.ofNullable(MinecraftServer.getConnectionManager().getPlayer(name)).map(MinestomEntityPlayer::new);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return Optional.ofNullable(MinecraftServer.getConnectionManager().getPlayer(uuid)).map(MinestomEntityPlayer::new);
    }

    @Override
    protected <T> CommandSenderWrapper wrapSender0(T sender) {
        if (sender instanceof CommandSender) {
            return new MinestomCommandSender(MinecraftServer.getCommandManager().getConsoleSender());
        }
        throw new UnsupportedOperationException("Can't wrap " + sender + " to CommandSenderWrapper");
    }

    @Override
    public SenderWrapper getConsoleSender0() {
        return new MinestomCommandSender(MinecraftServer.getCommandManager().getConsoleSender());
    }

    @Override
    public Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper) {
        return Optional.empty();
    }

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(CommandSender.class).hasPermission(((SimplePermission) permission).getPermissionString());
            } else {
                return ((SimplePermission) permission).isDefaultAllowed();
            }
        } else if (permission instanceof AndPermission) {
            return ((AndPermission) permission).getPermissions().stream().allMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof OrPermission) {
            return ((OrPermission) permission).getPermissions().stream().anyMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof PredicatePermission) {
            return permission.hasPermission(wrapper);
        }
        return false;
    }

    @Override
    public boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSender.class).getPermission(((SimplePermission) permission).getPermissionString()) != null;
        }
        return true;
    }

    @Override
    public boolean isOp0(Operator wrapper) {
        return false;
    }

    @Override
    public void setOp0(Operator wrapper, boolean op) {
        // empty stub
    }

    @Override
    public long getFirstPlayed0(OfflinePlayerWrapper playerWrapper) {
        return System.currentTimeMillis();
    }

    @Override
    public long getLastPlayed0(OfflinePlayerWrapper playerWrapper) {
        return System.currentTimeMillis();
    }

    @Override
    public boolean isBanned0(OfflinePlayerWrapper playerWrapper) {
        throw new UnsupportedOperationException("Minestom does not support bans");
    }

    @Override
    public boolean isWhitelisted0(OfflinePlayerWrapper playerWrapper) {
        throw new UnsupportedOperationException("Minestom does not support whitelists");
    }

    @Override
    public boolean isOnline0(OfflinePlayerWrapper playerWrapper) {
        var player = MinecraftServer.getConnectionManager().getPlayer(playerWrapper.getUuid());
        return player != null && player.isOnline();
    }

    @Override
    public void setWhitelisted0(OfflinePlayerWrapper playerWrapper, boolean whitelisted) {
        throw new UnsupportedOperationException("Minestom does not support whitelists");
    }

    @Override
    public OfflinePlayerWrapper getOfflinePlayer0(UUID uuid) {
        return new FinalOfflinePlayerWrapper(uuid, null);
    }

    @Override
    public Optional<OfflinePlayerWrapper> getOfflinePlayer0(String name) {
        return Optional.ofNullable(MinecraftServer.getConnectionManager().getPlayer(name)).map(MinestomEntityPlayer::new);
    }

    @Override
    public Optional<PlayerWrapper> getPlayerExact0(String name) {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(e -> e.getUsername().equals(name))
                .findFirst()
                .map(MinestomEntityPlayer::new);
    }
}
