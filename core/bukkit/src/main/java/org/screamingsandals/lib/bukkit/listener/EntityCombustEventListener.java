package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByBlockEvent;
import org.screamingsandals.lib.event.entity.SEntityCombustEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

public class EntityCombustEventListener extends AbstractBukkitEventHandlerFactory<EntityCombustEvent, SEntityCombustEvent> {

    public EntityCombustEventListener(Plugin plugin) {
        super(EntityCombustEvent.class, SEntityCombustEvent.class, plugin);
    }

    @Override
    protected SEntityCombustEvent wrapEvent(EntityCombustEvent event, EventPriority priority) {
        if (event instanceof EntityCombustByBlockEvent) {
            return new SEntityCombustByBlockEvent(
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ObjectLink.of(event::getDuration, event::setDuration),
                    ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(((EntityCombustByBlockEvent) event).getCombuster()))
            );
        }

        return new SEntityCombustEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getDuration, event::setDuration)
        );
    }
}
