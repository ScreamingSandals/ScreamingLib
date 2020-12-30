package org.screamingsandals.lib.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.lib.player.PlayerWrapper;

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
}
