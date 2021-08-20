package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Villager;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerCareerChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class VillagerCareerChangeEventListener extends AbstractBukkitEventHandlerFactory<VillagerCareerChangeEvent, SVillagerCareerChangeEvent> {

    public VillagerCareerChangeEventListener(Plugin plugin) {
        super(VillagerCareerChangeEvent.class, SVillagerCareerChangeEvent.class, plugin);
    }

    @Override
    protected SVillagerCareerChangeEvent wrapEvent(VillagerCareerChangeEvent event, EventPriority priority) {
        return new SVillagerCareerChangeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(
                        () -> SVillagerCareerChangeEvent.Profession.valueOf(event.getProfession().name().toUpperCase()),
                        profession -> event.setProfession(Villager.Profession.valueOf(profession.name().toUpperCase()))
                ),
                ImmutableObjectLink.of(() -> SVillagerCareerChangeEvent.ChangeReason.valueOf(event.getReason().name().toUpperCase()))
        );
    }
}
