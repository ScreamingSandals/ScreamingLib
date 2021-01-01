package org.screamingsandals.lib.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Optional;

public class BukkitPlayerUtils extends PlayerUtils {
    public static void init() {
        PlayerUtils.init(BukkitPlayerUtils::new);
    }

    public BukkitPlayerUtils() {
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Bukkit.getPlayer(playerWrapper.getUuid()));
    }

    @Override
    public void sendMessage0(PlayerWrapper playerWrapper, String message) {
        playerWrapper.as(Player.class).sendMessage(message);
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
}
