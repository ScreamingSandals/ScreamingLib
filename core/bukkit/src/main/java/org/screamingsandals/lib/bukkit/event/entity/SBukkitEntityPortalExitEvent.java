package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPortalExitEvent;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Data
public class SBukkitEntityPortalExitEvent implements SEntityPortalExitEvent {
    private final EntityPortalExitEvent event;

    // Internal cache
    private EntityBasic entity;
    private Vector3D before;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public LocationHolder getFrom() {
        return LocationMapper.wrapLocation(event.getFrom());
    }

    @Override
    public void setFrom(LocationHolder location) {
        event.setFrom(location.as(Location.class));
    }

    @Override
    public LocationHolder getTo() {
        return LocationMapper.wrapLocation(event.getTo());
    }

    @Override
    public void setTo(LocationHolder location) {
        event.setTo(location.as(Location.class));
    }

    @Override
    public Vector3D getBefore() {
        if (before == null) {
            before = new Vector3D(event.getBefore().getX(), event.getBefore().getY(), event.getBefore().getZ());
        }
        return before;
    }

    @Override
    public Vector3D getAfter() {
        return new Vector3D(event.getAfter().getX(), event.getAfter().getY(), event.getAfter().getZ());
    }

    @Override
    public void setAfter(Vector3D after) {
        event.setAfter(new Vector(after.getX(), after.getY(), after.getZ()));
    }
}
