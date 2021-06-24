package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SHorseJumpEvent;
import org.screamingsandals.lib.event.EventPriority;

@SuppressWarnings("deprecation")
public class HorseJumpEventListener extends AbstractBukkitEventHandlerFactory<HorseJumpEvent, SHorseJumpEvent> {

    public HorseJumpEventListener(Plugin plugin) {
        super(HorseJumpEvent.class, SHorseJumpEvent.class, plugin);
    }

    @Override
    protected SHorseJumpEvent wrapEvent(HorseJumpEvent event, EventPriority priority) {
        return new SHorseJumpEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getPower()
        );
    }

    @Override
    protected void postProcess(SHorseJumpEvent wrappedEvent, HorseJumpEvent event) {
        event.setPower(wrappedEvent.getPower());
    }
}
