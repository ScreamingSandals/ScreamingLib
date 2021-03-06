package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockPlaceEventListener {

    public PlayerBlockPlaceEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockPlaceEvent.class, event -> {
            final var result = EventManager.fire(new SPlayerBlockPlaceEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    PlayerMapper.wrapHand(event.getHand()),
                    BlockMapper.wrapBlock(event.getBlockPosition())
            ));

            event.setCancelled(result.isCancelled());
        });
    }
}
