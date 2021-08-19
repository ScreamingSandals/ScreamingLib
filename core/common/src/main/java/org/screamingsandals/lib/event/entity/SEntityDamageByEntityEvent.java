package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByEntityEvent extends SEntityDamageEvent {
    private final ImmutableObjectLink<EntityBasic> damager;
    
    public SEntityDamageByEntityEvent(ImmutableObjectLink<EntityBasic> damager, ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<DamageCauseHolder> damageCause, ObjectLink<Double> damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }

    public EntityBasic getDamager() {
        return damager.get();
    }
}
