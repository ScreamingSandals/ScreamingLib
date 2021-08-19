package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SExplosionPrimeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ObjectLink<Float> radius;
    private final ObjectLink<Boolean> fire;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public float getRadius() {
        return radius.get();
    }

    public void setRadius(float radius) {
        this.radius.set(radius);
    }

    public boolean isFire() {
        return fire.get();
    }

    public void setFire(boolean fire) {
        this.fire.set(fire);
    }
}
