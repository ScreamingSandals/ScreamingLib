package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityEnterLoveModeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityEnterLoveModeEvent implements SEntityEnterLoveModeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityEnterLoveModeEvent event;

    // Internal cache
    private EntityBasic entity;
    @Nullable
    private EntityBasic humanEntity;
    private boolean humanEntityCached;


    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public EntityBasic getHumanEntity() {
        if (!humanEntityCached) {
            if (event.getHumanEntity() != null) {
                humanEntity = EntityMapper.wrapEntity(event.getHumanEntity()).orElseThrow();
            }
            humanEntityCached = true;
        }
        return humanEntity;
    }

    @Override
    public int getTicksInLove() {
        return event.getTicksInLove();
    }

    @Override
    public void setTicksInLove(int ticksInLove) {
        event.setTicksInLove(ticksInLove);
    }
}
