package org.screamingsandals.lib.minestom.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.minestom.MinestomAudiences;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.minestom.player.event.*;
import org.screamingsandals.lib.minestom.utils.MinestomAdventureHelper;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class MinestomPlayerMapper extends PlayerMapper {

    public static void init(Extension extension) {
        PlayerMapper.init(() -> new MinestomPlayerMapper(extension));
    }

    public MinestomPlayerMapper(Extension extension) {
        registerEvents();
        MinestomAudiences.create(extension);

        playerConverter
                .registerW2P(Player.class, playerWrapper -> MinecraftServer.getConnectionManager()
                        .getPlayer(playerWrapper.getUuid()))
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getUsername(), player.getUuid()));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == CommandSenderWrapper.Type.PLAYER) {
                        final var player = MinecraftServer.getConnectionManager().getPlayer(wrapper.getName());
                        if (player == null) {
                            return null;
                        }
                        return PlayerMapper.wrapPlayer(player);
                    }
                    return null;
                })
                .registerW2P(CommandSender.class, wrapper -> {
                    switch (wrapper.getType()) {
                        case PLAYER:
                            return MinecraftServer.getConnectionManager().getPlayer(wrapper.getName());
                        case CONSOLE:
                            return MinecraftServer.getCommandManager().getConsoleSender();
                        default:
                            return null;
                    }
                })
                .registerP2W(CommandSender.class, sender -> {
                    if (sender.isPlayer()) {
                        return new SenderWrapper(sender.asPlayer().getUsername(), SenderWrapper.Type.PLAYER);
                    }
                    return new SenderWrapper(CONSOLE_NAME, SenderWrapper.Type.CONSOLE);
                });
        handConverter
                .registerW2P(Player.Hand.class, wrapper -> Player.Hand.valueOf(wrapper.name()))
                .registerP2W(Player.Hand.class, hand -> PlayerWrapper.Hand.valueOf(hand.name()));
    }

    @Override
    public void sendMessage0(SenderWrapper wrapper, String message) {
        if (wrapper.getName().equalsIgnoreCase(CONSOLE_NAME)) {
            MinecraftServer.getCommandManager().getConsoleSender().sendMessage(message);
        }

        wrapper.as(Player.class).sendMessage(message);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return playerConverter.convertOptional(MinecraftServer.getConnectionManager().getPlayer(name));
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return playerConverter.convertOptional(MinecraftServer.getConnectionManager().getPlayer(uuid));
    }

    @Override
    public CommandSenderWrapper getConsoleSender0() {
        return senderConverter.convert(MinecraftServer.getCommandManager().getConsoleSender());
    }

    @Override
    public List<PlayerWrapper> getPlayersOnServer0() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerWrapper> getPlayers0(WorldHolder holder) {
        return holder.as(Instance.class).getPlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void closeInventory0(PlayerWrapper playerWrapper) {
        playerWrapper.as(Player.class).closeInventory();
    }

    @Override
    public Container getPlayerInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getOpenInventory());
    }

    @Override
    public LocationHolder getLocation0(PlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(Player.class)).orElseThrow();
    }

    @Override
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        wrapper.as(Player.class).teleport(location.as(Position.class), callback);
    }

    @Override
    public void kick0(PlayerWrapper wrapper, Component message) {
        wrapper.as(Player.class).kick(MinestomAdventureHelper.toMinestom(message));
    }

    @Override
    public Audience getAudience0(CommandSenderWrapper wrapper) {
        final var audiences = (MinestomAudiences) provider;
        final var sender = wrapper.as(CommandSender.class);
        if (sender instanceof Player) {
            return audiences.player(((Player) sender).getUuid());
        } else if (sender instanceof ConsoleSender) {
            return audiences.console();
        }

        return Audience.empty();
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

    private void registerEvents() {
        new AsyncPlayerPreLoginListener();
        new PlayerJoinEventListener();
        new PlayerLeaveEventListener();
        new PlayerBlockPlaceEventListener();
        new PlayerBlockBreakEventListener();
        new PlayerMoveEventListener();
        new PlayerItemPickupEventListener();
    }
}
