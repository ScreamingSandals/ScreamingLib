package org.screamingsandals.lib.bukkit.player;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

import java.util.Optional;

@PlatformMapping(platform = PlatformType.BUKKIT)
public class BukkitPlayerMapper extends PlayerMapper {

    public static void init(Plugin plugin) {
        PlayerMapper.init(BukkitPlayerMapper::new, BukkitAudiences.create(plugin));
    }

    public BukkitPlayerMapper() {
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Bukkit.getPlayer(playerWrapper.getUuid()));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
                        final var player = Bukkit.getPlayer(wrapper.getName());
                        if (player == null) {
                            return null;
                        }
                        return new PlayerWrapper(player.getName(), player.getUniqueId());
                    }
                    return null;
                })
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
                        return new SenderWrapper(sender.getName(), SenderWrapper.Type.PLAYER);
                    }
                    return new SenderWrapper(sender.getName(), SenderWrapper.Type.CONSOLE);
                });
    }

    @Override
    public void sendMessage0(SenderWrapper wrapper, String message) {
        wrapper.as(CommandSender.class).sendMessage(message);
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
        return LocationMapping.resolve(playerWrapper.as(Player.class).getLocation()).orElseThrow();
    }

    @Override
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        PaperLib.teleportAsync(wrapper.as(Player.class), location.as(Location.class))
                .thenAccept(result -> {
                    if (result) {
                        //TODO: tasker
                        callback.run();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @Override
    protected Audience getAudience(SenderWrapper wrapper, AudienceProvider provider) {
        final var audiences = (BukkitAudiences) provider;
        final var sender = wrapper.as(CommandSender.class);
        if (sender instanceof Player) {
            return audiences.player(((Player) sender).getUniqueId());
        } else if (sender instanceof ConsoleCommandSender) {
            return audiences.console();
        }

        return Audience.empty();
    }
}
