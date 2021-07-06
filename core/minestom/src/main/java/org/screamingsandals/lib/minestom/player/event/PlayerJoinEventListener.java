package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerLoginEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;

public class PlayerJoinEventListener {

    public PlayerJoinEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerLoginEvent.class, event ->
                EventManager.fire(new SPlayerJoinEvent(
                        PlayerMapper.wrapPlayer(event.getPlayer())))
        );
    }
}
