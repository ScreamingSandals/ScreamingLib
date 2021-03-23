package org.screamingsandals.lib.entity.event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.DamageCause;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityDamageByBlockEvent extends SEntityDamageEvent {
    @Getter private final BlockHolder damager;

    public SEntityDamageByBlockEvent(@NotNull final BlockHolder damager, EntityBasic entity, DamageCause damageCause, double damage) {
        super(entity, damageCause, damage);
        this.damager = damager;
    }
}
