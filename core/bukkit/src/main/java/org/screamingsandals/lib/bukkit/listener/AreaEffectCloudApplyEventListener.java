package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SAreaEffectCloudApplyEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class AreaEffectCloudApplyEventListener extends AbstractBukkitEventHandlerFactory<AreaEffectCloudApplyEvent, SAreaEffectCloudApplyEvent> {

    public AreaEffectCloudApplyEventListener(Plugin plugin) {
        super(AreaEffectCloudApplyEvent.class, SAreaEffectCloudApplyEvent.class, plugin);
    }

    @Override
    protected SAreaEffectCloudApplyEvent wrapEvent(AreaEffectCloudApplyEvent event, EventPriority priority) {
        return new SAreaEffectCloudApplyEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                new CollectionLinkedToCollection<>(
                        event.getAffectedEntities(),
                        entityBasic -> entityBasic.as(LivingEntity.class),
                        livingEntity -> EntityMapper.wrapEntity(livingEntity).orElseThrow()
                )
        );
    }
}
