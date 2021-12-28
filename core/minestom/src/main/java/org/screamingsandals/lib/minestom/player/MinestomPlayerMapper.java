package org.screamingsandals.lib.minestom.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.minestom.entity.MinestomEntityPlayer;
import org.screamingsandals.lib.player.FinalOfflinePlayerWrapper;
import org.screamingsandals.lib.player.OfflinePlayerWrapper;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.UUID;

@Service
public class MinestomPlayerMapper extends PlayerMapper {
    public MinestomPlayerMapper(Extension extension) {
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
    public CommandSenderWrapper getConsoleSender0() {
        return senderConverter.convert(MinecraftServer.getCommandManager().getConsoleSender());
    }

    @Override
    public Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper) {
        return Optional.empty(); // TODO
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
        return false; // TODO: check if op exists in Minestom
    }

    @Override
    public void setOp0(Operator wrapper, boolean op) {
        // TODO: check if op exists in Minestom
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
}
