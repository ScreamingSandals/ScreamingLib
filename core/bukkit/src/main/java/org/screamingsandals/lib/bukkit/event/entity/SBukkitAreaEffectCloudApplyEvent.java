package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SAreaEffectCloudApplyEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitAreaEffectCloudApplyEvent implements SAreaEffectCloudApplyEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final AreaEffectCloudApplyEvent event;

    // Internal cache
    private EntityBasic entity;
    private Collection<EntityBasic> affectedEntities;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Collection<EntityBasic> getAffectedEntities() {
        if (affectedEntities == null) {
            affectedEntities = new CollectionLinkedToCollection<>(
                    event.getAffectedEntities(),
                    entityBasic -> entityBasic.as(LivingEntity.class),
                    livingEntity -> EntityMapper.wrapEntity(livingEntity).orElseThrow()
            );
        }
        return affectedEntities;
    }
}
