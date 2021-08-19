package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockDamageEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockDamageEventListener extends AbstractBukkitEventHandlerFactory<BlockDamageEvent, SPlayerBlockDamageEvent> {

    public PlayerBlockDamageEventListener(Plugin plugin) {
        super(BlockDamageEvent.class, SPlayerBlockDamageEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockDamageEvent wrapEvent(BlockDamageEvent event, EventPriority priority) {
        return new SPlayerBlockDamageEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getInstaBreak()).orElseThrow()),
                ObjectLink.of(event::getInstaBreak, event::setInstaBreak)
        );
    }
}
