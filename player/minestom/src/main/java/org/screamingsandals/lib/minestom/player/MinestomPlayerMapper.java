package org.screamingsandals.lib.minestom.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.minestom.MinestomAudiences;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import net.minestom.server.utils.Position;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.minestom.player.event.AsyncPlayerPreLoginListener;
import org.screamingsandals.lib.minestom.player.event.PlayerJoinEventListener;
import org.screamingsandals.lib.minestom.player.event.PlayerLeaveEventListener;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

import java.util.Optional;

@Service
public class MinestomPlayerMapper extends PlayerMapper {

    public static void init(Extension extension) {
        PlayerMapper.init(MinestomPlayerMapper::new, MinestomAudiences.create(extension));
    }

    public MinestomPlayerMapper() {
        registerEvents();
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getUsername(), player.getUuid()))
                .registerW2P(Player.class, playerWrapper -> MinecraftServer.getConnectionManager()
                        .getPlayer(playerWrapper.getUuid()));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
                        final var player = MinecraftServer.getConnectionManager().getPlayer(wrapper.getName());
                        if (player == null) {
                            return null;
                        }
                        return new PlayerWrapper(player.getUsername(), player.getUuid());
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

    @Override
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        wrapper.as(Player.class).teleport(location.as(Position.class), callback);
    }

    @Override
    protected Audience getAudience(SenderWrapper wrapper, AudienceProvider provider) {
        final var audiences = (MinestomAudiences) provider;
        final var sender = wrapper.as(CommandSender.class);
        if (sender instanceof Player) {
            return audiences.player(((Player) sender).getUuid());
        } else if (sender instanceof ConsoleSender) {
            return audiences.console();
        }

        return Audience.empty();
    }

    private void registerEvents() {
        new AsyncPlayerPreLoginListener();
        new PlayerJoinEventListener();
        new PlayerLeaveEventListener();
    }
}
