package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityExplodeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<LocationHolder> location;
    private final Collection<BlockHolder> blocks;
    private final ObjectLink<Float> yield;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public LocationHolder getLocation() {
        return location.get();
    }

    public float getYield() {
        return yield.get();
    }

    public void setYield(float yield) {
        this.yield.set(yield);
    }
}
