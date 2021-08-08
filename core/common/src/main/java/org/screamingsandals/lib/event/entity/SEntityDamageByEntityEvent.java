package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByEntityEvent extends SEntityDamageEvent {
    @Getter private final EntityBasic damager;
    
    public SEntityDamageByEntityEvent(@NotNull final EntityBasic damager, EntityBasic entity, DamageCauseHolder damageCause, double damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }
}
