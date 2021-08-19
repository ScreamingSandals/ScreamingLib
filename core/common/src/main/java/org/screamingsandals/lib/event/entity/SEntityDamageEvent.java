package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityDamageEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<DamageCauseHolder> damageCause;
    private final ObjectLink<Double> damage;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public DamageCauseHolder getDamageCause() {
        return damageCause.get();
    }

    public double getDamage() {
        return damage.get();
    }

    public void setDamage(double damage) {
        this.damage.set(damage);
    }
}
