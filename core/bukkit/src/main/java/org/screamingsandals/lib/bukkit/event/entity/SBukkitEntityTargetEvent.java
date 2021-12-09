package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTargetEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityTargetEvent implements SEntityTargetEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityTargetEvent event;

    // Internal cache
    private EntityBasic entity;
    private TargetReason targetReason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public EntityBasic getTarget() {
        if (event.getTarget() == null) {
            return null;
        }

        return EntityMapper.wrapEntity(event.getTarget()).orElseThrow();
    }

    @Override
    public void setTarget(@Nullable EntityBasic target) {
        event.setTarget(target == null ? null : target.as(Entity.class));
    }

    @Override
    public TargetReason getTargetReason() {
        if (targetReason == null) {
            targetReason = TargetReason.valueOf(event.getReason().name());
        }
        return targetReason;
    }
}
