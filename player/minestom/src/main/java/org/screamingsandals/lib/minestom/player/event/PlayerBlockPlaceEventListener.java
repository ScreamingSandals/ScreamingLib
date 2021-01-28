package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockPlaceEventListener {

    public PlayerBlockPlaceEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockPlaceEvent.class, event ->
                EventManager.fire(new SPlayerBlockPlaceEvent(
                        PlayerMapper.wrapPlayer(event.getPlayer()),
                        PlayerMapper.wrapHand(event.getHand()),
                        BlockMapper.wrapBlock(event.getBlockPosition())
                )));
    }
}
