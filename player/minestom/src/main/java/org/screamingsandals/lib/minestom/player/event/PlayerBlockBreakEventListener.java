package org.screamingsandals.lib.minestom.player.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockBreakEventListener {

    public PlayerBlockBreakEventListener() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockBreakEvent.class, event -> {
            final var result = EventManager.fire(new SPlayerBlockBreakEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    BlockMapper.wrapBlock(event.getBlockPosition()),
                    true) //probably shouldn't be hardcoded
            );

            event.setCancelled(result.isCancelled());
        });
    }
}
