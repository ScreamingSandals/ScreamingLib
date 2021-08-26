package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

public class BlockExperienceEventListener extends AbstractBukkitEventHandlerFactory<BlockExpEvent, SBlockExperienceEvent> {

    public BlockExperienceEventListener(Plugin plugin) {
        super(BlockExpEvent.class, SBlockExperienceEvent.class, plugin);
    }

    @Override
    protected SBlockExperienceEvent wrapEvent(BlockExpEvent event, EventPriority priority) {
        if (event instanceof BlockBreakEvent) {
            return new SPlayerBlockBreakEvent(
                    ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(((BlockBreakEvent) event).getPlayer())),
                    ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                    ObjectLink.of(((BlockBreakEvent) event)::isDropItems, ((BlockBreakEvent) event)::setDropItems),
                    ObjectLink.of(event::getExpToDrop, event::setExpToDrop)
            );
        }

        return new SBlockExperienceEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ObjectLink.of(event::getExpToDrop, event::setExpToDrop)
        );
    }
}
