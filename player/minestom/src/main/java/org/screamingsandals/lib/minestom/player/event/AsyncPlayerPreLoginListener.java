package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.net.InetSocketAddress;

public class AsyncPlayerPreLoginListener {

    public AsyncPlayerPreLoginListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(AsyncPlayerPreLoginEvent.class, callback -> {
            final var player = callback.getPlayer();
            final var remoteAddress = (InetSocketAddress) callback.getPlayer().getPlayerConnection().getRemoteAddress();
            final var toFire = new org.screamingsandals.lib.player.event.AsyncPlayerPreLoginEvent(
                    callback.getPlayerUuid(), callback.getUsername(), remoteAddress.getAddress());
            try {
                final var result = EventManager.fireAsync(toFire).get();
                if (result.getResult() != org.screamingsandals.lib.player.event.AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                    player.kick((AdventureHelper.toLegacy(result.getMessage())));
                    return;
                }

                final var name = result.getName();
                if (!name.equalsIgnoreCase(callback.getUsername())) {
                    callback.setUsername(name);
                }

            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}
