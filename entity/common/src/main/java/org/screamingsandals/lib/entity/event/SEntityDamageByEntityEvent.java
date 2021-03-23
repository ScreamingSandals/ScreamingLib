package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.DamageCause;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByEntityEvent extends SEntityDamageEvent {
    @Getter private final EntityBasic damager;
    
    public SEntityDamageByEntityEvent(@NotNull final EntityBasic damager, EntityBasic entity, DamageCause damageCause, double damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }
}
