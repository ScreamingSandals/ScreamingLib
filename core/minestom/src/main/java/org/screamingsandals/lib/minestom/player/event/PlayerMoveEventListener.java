package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerMoveEventListener {

    public PlayerMoveEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerMoveEvent.class, event -> {
            final var toFire = new SPlayerMoveEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    LocationMapper.wrapLocation(event.getPlayer().getPosition()),
                    LocationMapper.wrapLocation(event.getNewPosition())
            );

            EventManager.fire(toFire);
            event.setCancelled(toFire.isCancelled());
        });
    }
}
