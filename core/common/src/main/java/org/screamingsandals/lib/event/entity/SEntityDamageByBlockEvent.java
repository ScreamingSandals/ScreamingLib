package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByBlockEvent extends SEntityDamageEvent {
    @Getter
    @Nullable
    private final BlockHolder damager;

    public SEntityDamageByBlockEvent(@Nullable BlockHolder damager, EntityBasic entity, DamageCauseHolder damageCause, double damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }
}
