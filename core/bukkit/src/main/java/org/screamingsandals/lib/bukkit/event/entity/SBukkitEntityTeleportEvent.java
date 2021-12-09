package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTeleportEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;


@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityTeleportEvent implements SEntityTeleportEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityTeleportEvent event;

    // Internal cache
    private EntityBasic entity;

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
    public void setFrom(LocationHolder from) {
        event.setFrom(from.as(Location.class));
    }

    @Override
    public LocationHolder getTo() {
        return LocationMapper.wrapLocation(event.getTo());
    }

    @Override
    public void setTo(LocationHolder to) {
        event.setFrom(to.as(Location.class));
    }
}
