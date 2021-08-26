package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByBlockEvent extends SEntityDamageEvent {
    private final ImmutableObjectLink<@Nullable BlockHolder> damager;

    public SEntityDamageByBlockEvent(ImmutableObjectLink<@Nullable BlockHolder> damager, ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<DamageCauseHolder> damageCause, ObjectLink<Double> damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }

    @Nullable
    public BlockHolder getDamager() {
        return damager.get();
    }
}
