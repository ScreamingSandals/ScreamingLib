package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLeaveEvent;

public class PlayerLeaveEventListener {

    public PlayerLeaveEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerDisconnectEvent.class, event ->
                EventManager.fire(new SPlayerLeaveEvent(
                        PlayerMapper.wrapPlayer(event.getPlayer())))
        );
    }

}
