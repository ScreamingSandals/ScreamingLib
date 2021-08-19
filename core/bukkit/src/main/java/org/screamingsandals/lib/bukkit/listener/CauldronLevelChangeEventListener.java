package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SCauldronLevelChangeEvent;

public class CauldronLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<CauldronLevelChangeEvent, SCauldronLevelChangeEvent> {

    public CauldronLevelChangeEventListener(Plugin plugin) {
        super(CauldronLevelChangeEvent.class, SCauldronLevelChangeEvent.class, plugin);
    }

    @Override
    protected SCauldronLevelChangeEvent wrapEvent(CauldronLevelChangeEvent event, EventPriority priority) {
        return new SCauldronLevelChangeEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::getOldLevel),
                ImmutableObjectLink.of(() -> SCauldronLevelChangeEvent.Reason.valueOf(event.getReason().name())),
                ObjectLink.of(event::getNewLevel, event::setNewLevel)
        );
    }
}
