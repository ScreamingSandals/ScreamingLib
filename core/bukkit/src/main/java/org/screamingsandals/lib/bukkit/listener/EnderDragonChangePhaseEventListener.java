package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEnderDragonChangePhaseEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EnderDragonChangePhaseEventListener extends AbstractBukkitEventHandlerFactory<EnderDragonChangePhaseEvent, SEnderDragonChangePhaseEvent> {

    public EnderDragonChangePhaseEventListener(Plugin plugin) {
        super(EnderDragonChangePhaseEvent.class, SEnderDragonChangePhaseEvent.class, plugin);
    }

    @Override
    protected SEnderDragonChangePhaseEvent wrapEvent(EnderDragonChangePhaseEvent event, EventPriority priority) {
        return new SEnderDragonChangePhaseEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> event.getCurrentPhase() != null ?
                        SEnderDragonChangePhaseEvent.Phase.valueOf(event.getCurrentPhase().name().toUpperCase()) : null),
                ObjectLink.of(
                        () -> SEnderDragonChangePhaseEvent.Phase.valueOf(event.getNewPhase().name().toUpperCase()),
                        phase -> event.setNewPhase(EnderDragon.Phase.valueOf(phase.name().toUpperCase()))
                )
        );
    }
}
