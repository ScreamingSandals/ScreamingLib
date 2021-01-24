package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerLoginEvent;

public class PlayerJoinEventListener {

    public PlayerJoinEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerLoginEvent.class, event -> {
            //TODO
        });
    }
}
