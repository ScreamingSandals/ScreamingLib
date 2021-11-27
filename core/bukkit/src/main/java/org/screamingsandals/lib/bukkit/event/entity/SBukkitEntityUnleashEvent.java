package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityUnleashEvent;

@Data
public class SBukkitEntityUnleashEvent implements SEntityUnleashEvent {
    private final EntityUnleashEvent event;

    // Internal cache
    private EntityBasic entity;
    private UnleashReason reason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public UnleashReason getReason() {
        if (reason == null) {
            reason = UnleashReason.valueOf(event.getReason().name());
        }
        return reason;
    }
}
