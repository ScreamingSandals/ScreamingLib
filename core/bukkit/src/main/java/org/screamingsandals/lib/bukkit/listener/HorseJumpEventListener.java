package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SHorseJumpEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@SuppressWarnings("deprecation")
public class HorseJumpEventListener extends AbstractBukkitEventHandlerFactory<HorseJumpEvent, SHorseJumpEvent> {

    public HorseJumpEventListener(Plugin plugin) {
        super(HorseJumpEvent.class, SHorseJumpEvent.class, plugin);
    }

    @Override
    protected SHorseJumpEvent wrapEvent(HorseJumpEvent event, EventPriority priority) {
        return new SHorseJumpEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getPower, event::setPower)
        );
    }
}
