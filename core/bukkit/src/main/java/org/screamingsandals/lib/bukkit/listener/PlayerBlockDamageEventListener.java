package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockDamageEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockDamageEventListener extends AbstractBukkitEventHandlerFactory<BlockDamageEvent, SPlayerBlockDamageEvent> {

    public PlayerBlockDamageEventListener(Plugin plugin) {
        super(BlockDamageEvent.class, SPlayerBlockDamageEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockDamageEvent wrapEvent(BlockDamageEvent event, EventPriority priority) {
        return new SPlayerBlockDamageEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                BlockMapper.wrapBlock(event.getBlock()),
                ItemFactory.build(event.getInstaBreak()).orElseThrow(),
                event.getInstaBreak()
        );
    }

    @Override
    protected void postProcess(SPlayerBlockDamageEvent wrappedEvent, BlockDamageEvent event) {
        event.setInstaBreak(wrappedEvent.isInstantBreak());
    }
}
