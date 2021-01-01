package org.screamingsandals.lib.minestom.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Optional;

public class MinestomPlayerUtils extends PlayerUtils {
    public static void init() {
        PlayerUtils.init(MinestomPlayerUtils::new);
    }

    public MinestomPlayerUtils() {
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getUsername(), player.getUuid()))
                .registerW2P(Player.class, playerWrapper -> MinecraftServer.getConnectionManager().getPlayer(playerWrapper.getUuid()));
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
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getOpenInventory());
    }
}
