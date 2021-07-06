package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.*;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerChatEvent;

import java.util.stream.Collectors;

public class PlayerChatEventListener {

    public PlayerChatEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerChatEvent.class, event -> {
            final var result = EventManager.fire(new SPlayerChatEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    event.getMessage(),
                    null,
                    event.getRecipients().stream().map(PlayerMapper::wrapPlayer).collect(Collectors.toList())
            ));

            event.setCancelled(result.isCancelled());
            event.setMessage(result.getMessage());
            if (result.getFormat() != null) {
                event.setChatFormat(playerChatEvent -> buildDefaultChatMessage(playerChatEvent, result.getFormat()));
            }
            event.getRecipients().clear();
            event.getRecipients().addAll(result.getRecipients().stream().map(w -> w.as(Player.class)).collect(Collectors.toList()));
        });
    }

    private static JsonMessage buildDefaultChatMessage(PlayerChatEvent chatEvent, String format) {
        final String username = chatEvent.getPlayer().getUsername();

        return ColoredText.of(String.format(format, username, chatEvent.getMessage()));
    }
}
