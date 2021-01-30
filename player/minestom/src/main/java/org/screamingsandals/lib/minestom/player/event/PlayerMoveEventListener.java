package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerMoveEvent;
import org.screamingsandals.lib.world.LocationMapping;

public class PlayerMoveEventListener {

    public PlayerMoveEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerMoveEvent.class, event -> {
            final var toFire = new SPlayerMoveEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    LocationMapping.wrapLocation(event.getPlayer().getPosition()),
                    LocationMapping.wrapLocation(event.getNewPosition())
            );

            EventManager.fire(toFire);
            event.setCancelled(toFire.isCancelled());
        });
    }
}
