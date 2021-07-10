package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SAreaEffectCloudApplyEvent;
import org.screamingsandals.lib.event.EventPriority;

import java.util.stream.Collectors;

public class AreaEffectCloudApplyEventListener extends AbstractBukkitEventHandlerFactory<AreaEffectCloudApplyEvent, SAreaEffectCloudApplyEvent> {

    public AreaEffectCloudApplyEventListener(Plugin plugin) {
        super(AreaEffectCloudApplyEvent.class, SAreaEffectCloudApplyEvent.class, plugin);
    }

    @Override
    protected SAreaEffectCloudApplyEvent wrapEvent(AreaEffectCloudApplyEvent event, EventPriority priority) {
        return new SAreaEffectCloudApplyEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getAffectedEntities()
                        .stream()
                        .map(entity -> EntityMapper.wrapEntity(entity).orElseThrow())
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void postProcess(SAreaEffectCloudApplyEvent wrappedEvent, AreaEffectCloudApplyEvent event) {
        event.getAffectedEntities().clear();
        wrappedEvent
                .getAffectedEntities()
                .stream()
                .map(entityBasic -> entityBasic.as(LivingEntity.class))
                .forEach(event.getAffectedEntities()::add);
    }
}
