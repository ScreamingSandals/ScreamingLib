package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCreatePortalEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.block.state.BlockStateMapper;

public class EntityCreatePortalEventListener extends AbstractBukkitEventHandlerFactory<EntityCreatePortalEvent, SEntityCreatePortalEvent> {

    public EntityCreatePortalEventListener(Plugin plugin) {
        super(EntityCreatePortalEvent.class, SEntityCreatePortalEvent.class, plugin);
    }

    @Override
    protected SEntityCreatePortalEvent wrapEvent(EntityCreatePortalEvent event, EventPriority priority) {
        return new SEntityCreatePortalEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                new CollectionLinkedToCollection<>( // is this supposed to be mutable or not? I'm not sure
                        event.getBlocks(),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow()
                ),
                ImmutableObjectLink.of(() -> PortalType.valueOf(event.getPortalType().name().toUpperCase()))
        );
    }
}
