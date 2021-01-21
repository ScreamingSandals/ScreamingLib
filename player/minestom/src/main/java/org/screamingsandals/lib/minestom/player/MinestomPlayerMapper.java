package org.screamingsandals.lib.minestom.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AutoInitialization;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

import java.util.Optional;

@AutoInitialization(platform = PlatformType.MINESTOM)
public class MinestomPlayerMapper extends PlayerMapper {
    public static void init() {
        PlayerMapper.init(MinestomPlayerMapper::new);
    }

    public MinestomPlayerMapper() {
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getUsername(), player.getUuid()))
                .registerW2P(Player.class, playerWrapper -> MinecraftServer.getConnectionManager()
                        .getPlayer(playerWrapper.getUuid()));
        senderConverter
                .registerW2P(CommandSender.class, wrapper -> {
                    final var name = wrapper.getName();
                    if (name.equalsIgnoreCase(CONSOLE_NAME)) {
                        return MinecraftServer.getCommandManager().getConsoleSender();
                    }
                    return MinecraftServer.getConnectionManager().getPlayer(name);
                })
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    final var name = wrapper.getName();
                    if (name.equalsIgnoreCase(CONSOLE_NAME)) {
                        return null;
                    }

                    final var player = MinecraftServer.getConnectionManager().getPlayer(name);
                    if (player == null) {
                        return null;
                    }

                    return new PlayerWrapper(player.getUsername(), player.getUuid());
                })
                .registerP2W(CommandSender.class, sender -> {
                    if (sender.isPlayer()) {
                        return new SenderWrapper(sender.asPlayer().getUsername(), SenderWrapper.Type.PLAYER);
                    }
                    return new SenderWrapper(CONSOLE_NAME, SenderWrapper.Type.CONSOLE);
                });
    }

    @Override
    public void sendMessage0(SenderWrapper wrapper, String message) {
        if (wrapper.getName().equalsIgnoreCase(CONSOLE_NAME)) {
            MinecraftServer.getCommandManager().getConsoleSender().sendMessage(message);
        }

        wrapper.as(Player.class).sendMessage(message);
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
        return LocationMapping.resolve(playerWrapper.as(Player.class)).orElseThrow();
    }
}
