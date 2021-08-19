package org.screamingsandals.lib.event.entity;

import lombok.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityPortalEvent extends SEntityTeleportEvent {
    private final ObjectLink<Integer> searchRadius;

    public SEntityPortalEvent(ImmutableObjectLink<EntityBasic> entity, ObjectLink<LocationHolder> from, ObjectLink<LocationHolder> to, ObjectLink<Integer> searchRadius) {
        super(entity, from, to);
        this.searchRadius = searchRadius;
    }

    public int getSearchRadius() {
        return searchRadius.get();
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius.set(searchRadius);
    }
}
