package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SItemDespawnEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Data
public class SBukkitItemDespawnEvent implements SItemDespawnEvent, BukkitCancellable {
    private final ItemDespawnEvent event;

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
