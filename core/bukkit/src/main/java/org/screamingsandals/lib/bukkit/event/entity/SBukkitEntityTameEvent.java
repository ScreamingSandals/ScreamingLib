package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityTameEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTameEvent;

@Data
public class SBukkitEntityTameEvent implements SEntityTameEvent, BukkitCancellable {
    private final EntityTameEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityBasic owner;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public EntityBasic getOwner() {
        if (owner == null) {
            owner = EntityMapper.wrapEntity(event.getOwner()).orElseThrow();
        }
        return owner;
    }
}
