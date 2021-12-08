package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPortalEnterEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Data
public class SBukkitEntityPortalEnterEvent implements SEntityPortalEnterEvent {
    private final EntityPortalEnterEvent event;

    // Internal cache
    private EntityBasic entity;
    private LocationHolder location;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public LocationHolder getLocation() {
        if (location == null) {
            location = LocationMapper.wrapLocation(event.getLocation());
        }
        return location;
    }
}
