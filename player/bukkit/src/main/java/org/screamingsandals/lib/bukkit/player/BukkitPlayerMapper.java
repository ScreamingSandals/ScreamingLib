package org.screamingsandals.lib.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
    public static void init() {
        PlayerMapper.init(BukkitPlayerMapper::new);
    }

    public BukkitPlayerMapper() {
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Bukkit.getPlayer(playerWrapper.getUuid()));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getName().equalsIgnoreCase(CONSOLE_NAME)) {
                        return null;
                    }
                    final var player = Bukkit.getPlayer(wrapper.getName());
                    if (player == null) {
                        return null;
                    }

                    return new PlayerWrapper(player.getName(), player.getUniqueId());
                })
                .registerW2P(CommandSender.class, wrapper -> {
                    if (wrapper.getName().equalsIgnoreCase(CONSOLE_NAME)) {
                        return Bukkit.getConsoleSender();
                    }
                    return Bukkit.getPlayer(wrapper.getName());
                })
                .registerP2W(CommandSender.class, sender -> {
                    final var name = sender.getName();
                    if (name.equalsIgnoreCase(CONSOLE_NAME)) {
                        return new SenderWrapper(sender.getName(), SenderWrapper.Type.CONSOLE);
                    }

                    return new SenderWrapper(sender.getName(), SenderWrapper.Type.PLAYER);
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
}
