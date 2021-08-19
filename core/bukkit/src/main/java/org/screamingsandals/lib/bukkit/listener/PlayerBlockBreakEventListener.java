package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBlockBreakEventListener extends AbstractBukkitEventHandlerFactory<BlockBreakEvent, SPlayerBlockBreakEvent> {

    public PlayerBlockBreakEventListener(Plugin plugin) {
        super(BlockBreakEvent.class, SPlayerBlockBreakEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockBreakEvent wrapEvent(BlockBreakEvent event, EventPriority priority) {
        return new SPlayerBlockBreakEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ObjectLink.of(event::isDropItems, event::setDropItems),
                ObjectLink.of(event::getExpToDrop, event::setExpToDrop)
        );
    }
}
