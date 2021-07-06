package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCreatePortalEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.stream.Collectors;

public class EntityCreatePortalEventListener extends AbstractBukkitEventHandlerFactory<EntityCreatePortalEvent, SEntityCreatePortalEvent> {

    public EntityCreatePortalEventListener(Plugin plugin) {
        super(EntityCreatePortalEvent.class, SEntityCreatePortalEvent.class, plugin);
    }

    @Override
    protected SEntityCreatePortalEvent wrapEvent(EntityCreatePortalEvent event, EventPriority priority) {
        return new SEntityCreatePortalEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getBlocks()
                        .stream()
                        .map(blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow())
                        .collect(Collectors.toList()),
                PortalType.valueOf(event.getPortalType().name().toUpperCase())
        );
    }

    @Override
    protected void postProcess(SEntityCreatePortalEvent wrappedEvent, EntityCreatePortalEvent event) {
        event.getBlocks().clear();
        wrappedEvent
                .getBlocks()
                .stream()
                .map(blockStateHolder -> blockStateHolder.as(BlockState.class))
                .forEach(event.getBlocks()::add);
    }
}
