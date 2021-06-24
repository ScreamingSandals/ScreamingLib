package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEnderDragonChangePhaseEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EnderDragonChangePhaseEventListener extends AbstractBukkitEventHandlerFactory<EnderDragonChangePhaseEvent, SEnderDragonChangePhaseEvent> {

    public EnderDragonChangePhaseEventListener(Plugin plugin) {
        super(EnderDragonChangePhaseEvent.class, SEnderDragonChangePhaseEvent.class, plugin);
    }

    @Override
    protected SEnderDragonChangePhaseEvent wrapEvent(EnderDragonChangePhaseEvent event, EventPriority priority) {
        final var currentPhase = event.getCurrentPhase() != null ?
                SEnderDragonChangePhaseEvent.Phase.valueOf(event.getCurrentPhase().name().toUpperCase()) : null;

        return new SEnderDragonChangePhaseEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                currentPhase,
                SEnderDragonChangePhaseEvent.Phase.valueOf(event.getNewPhase().name().toUpperCase())
        );
    }

    @Override
    protected void postProcess(SEnderDragonChangePhaseEvent wrappedEvent, EnderDragonChangePhaseEvent event) {
        event.setNewPhase(EnderDragon.Phase.valueOf(wrappedEvent.getNewPhase().name().toUpperCase()));
    }
}
