package org.screamingsandals.lib.event.entity;

import lombok.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityPortalEvent extends SEntityTeleportEvent {
    @Getter @Setter
    private int searchRadius = 128;

    public SEntityPortalEvent(EntityBasic entity, LocationHolder from, LocationHolder to) {
        super(entity, from, to);
    }
}
