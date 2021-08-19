package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityTeleportEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ObjectLink<LocationHolder> from;
    private final ObjectLink<LocationHolder> to;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public LocationHolder getFrom() {
        return from.get();
    }

    public void setFrom(LocationHolder from) {
        this.from.set(from);
    }

    public LocationHolder getTo() {
        return to.get();
    }

    public void setTo(LocationHolder to) {
        this.to.set(to);
    }
}
