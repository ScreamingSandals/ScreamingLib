package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockPlaceEventListener extends AbstractBukkitEventHandlerFactory<BlockPlaceEvent, SPlayerBlockPlaceEvent> {

    public PlayerBlockPlaceEventListener(Plugin plugin) {
        super(BlockPlaceEvent.class, SPlayerBlockPlaceEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockPlaceEvent wrapEvent(BlockPlaceEvent event, EventPriority priority) {
        return new SPlayerBlockPlaceEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                PlayerMapper.wrapHand(event.getHand()),
                BlockMapper.wrapBlock(event.getBlock())
        );
    }
}
