package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityResurrectEvent;

@Data
public class SBukkitEntityResurrectEvent implements SEntityResurrectEvent, BukkitCancellable {
    private final EntityResurrectEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }
}
