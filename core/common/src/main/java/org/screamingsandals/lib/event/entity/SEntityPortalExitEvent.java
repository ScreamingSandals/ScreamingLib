package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPortalExitEvent extends AbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<Vector3D> before;
    private final ObjectLink<Vector3D> after;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public Vector3D getBefore() {
        return before.get();
    }

    public Vector3D getAfter() {
        return after.get();
    }

    public void setAfter(Vector3D after) {
        this.after.set(after);
    }
}
