package org.screamingsandals.lib.bukkit.player;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.player.listener.*;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BukkitPlayerMapper extends PlayerMapper {
    public static void init(Plugin plugin, Controllable controllable) {
        PlayerMapper.init(() -> new BukkitPlayerMapper(plugin, controllable));
    }

    public BukkitPlayerMapper(Plugin plugin, Controllable controllable) {
        controllable.enable(() -> {
            provider = BukkitAudiences.create(plugin);
            registerListeners(plugin);
        });

        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> {
                    if (playerWrapper.getWrappedPlayer() != null) {
                        final var maybePlayer = playerWrapper.getWrappedPlayer().get();
                        if (maybePlayer != null) {
                            return (Player) maybePlayer;
                        }
                    }

                    return Bukkit.getPlayer(playerWrapper.getUuid());
                });
        senderConverter
                .registerW2P(Player.class, wrapper -> {
                    if (wrapper.getType() != CommandSenderWrapper.Type.PLAYER) {
                        return null;
                    }

                    if (wrapper instanceof PlayerWrapper) {
                        final var wrapped = ((PlayerWrapper) wrapper).getWrappedPlayer().get();
                        if (wrapped != null) {
                            return (Player) wrapped;
                        }
                    }

                    return Bukkit.getPlayer(wrapper.getName());
                })
                .registerP2W(Player.class, player -> new SenderWrapper(player.getName(), CommandSenderWrapper.Type.PLAYER))
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == CommandSenderWrapper.Type.PLAYER) {
                        final var player = Bukkit.getPlayer(wrapper.getName());
                        if (player == null) {
                            return null;
                        }
                        return PlayerMapper.wrapPlayer(player);
                    }
                    return null;
                })
                .registerP2W(PlayerWrapper.class, wrapper -> wrapper)
                .registerW2P(CommandSender.class, wrapper -> {
                    switch (wrapper.getType()) {
                        case PLAYER:
                            return Bukkit.getPlayer(wrapper.getName());
                        case CONSOLE:
                            return Bukkit.getConsoleSender();
                        default:
                            return null;
                    }
                })
                .registerP2W(CommandSender.class, sender -> {
                    if (sender instanceof Player) {
                        return PlayerMapper.wrapPlayer(sender);
                    }
                    return new SenderWrapper(sender.getName(), CommandSenderWrapper.Type.CONSOLE);
                });
        handConverter
                .registerW2P(EquipmentSlot.class, wrapper -> {
                    if (wrapper == PlayerWrapper.Hand.OFF) {
                        return EquipmentSlot.OFF_HAND;
                    }
                    return EquipmentSlot.HAND;
                })
                .registerP2W(EquipmentSlot.class, hand -> {
                    if (hand == EquipmentSlot.OFF_HAND) {
                        return PlayerWrapper.Hand.OFF;
                    }
                    return PlayerWrapper.Hand.MAIN;
                });
    }

    @Override
    public void sendMessage0(SenderWrapper wrapper, String message) {
        wrapper.as(CommandSender.class).sendMessage(message);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return playerConverter.convertOptional(Bukkit.getPlayer(name));
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return playerConverter.convertOptional(Bukkit.getPlayer(uuid));
    }

    @Override
    public CommandSenderWrapper getConsoleSender0() {
        return senderConverter.convert(Bukkit.getConsoleSender());
    }

    @Override
    public List<PlayerWrapper> getPlayers0() {
        return Bukkit.getOnlinePlayers().stream()
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
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getOpenInventory().getTopInventory());
    }

    @Override
    public LocationHolder getLocation0(PlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(Player.class).getLocation()).orElseThrow();
    }

    @Override
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        PaperLib.teleportAsync(wrapper.as(Player.class), location.as(Location.class))
                .thenAccept(result -> {
                    if (result) {
                        callback.run();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @Override
    public void kick0(PlayerWrapper wrapper, Component message) {
        wrapper.as(Player.class).kickPlayer(AdventureHelper.toLegacy(message));
    }

    @Override
    public Audience getAudience0(CommandSenderWrapper wrapper) {
        final var audiences = (BukkitAudiences) provider;
        if (wrapper.getType() == CommandSenderWrapper.Type.CONSOLE) {
            return audiences.console();
        }

        return audiences.player(wrapper.as(Player.class).getUniqueId());
    }

    private void registerListeners(Plugin plugin) {
        new AsyncPlayerPreLoginEventListener(plugin);
        new PlayerJoinEventListener(plugin);
        new PlayerLeaveEventListener(plugin);
        new PlayerBlockPlaceEventListener(plugin);
        new PlayerBlockBreakEventListener(plugin);
        new PlayerMoveEventListener(plugin);
        new PlayerPickupItemListener(plugin);
    }
}
