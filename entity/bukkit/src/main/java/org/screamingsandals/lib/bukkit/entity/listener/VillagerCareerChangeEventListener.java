package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SVillagerCareerChangeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class VillagerCareerChangeEventListener extends AbstractBukkitEventHandlerFactory<VillagerCareerChangeEvent, SVillagerCareerChangeEvent> {

    public VillagerCareerChangeEventListener(Plugin plugin) {
        super(VillagerCareerChangeEvent.class, SVillagerCareerChangeEvent.class, plugin);
    }

    @Override
    protected SVillagerCareerChangeEvent wrapEvent(VillagerCareerChangeEvent event, EventPriority priority) {
        return new SVillagerCareerChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                SVillagerCareerChangeEvent.Profession.valueOf(event.getProfession().name().toUpperCase()),
                SVillagerCareerChangeEvent.ChangeReason.valueOf(event.getReason().name().toUpperCase())
        );
    }
}
