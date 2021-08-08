package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.event.CancellableAbstractEvent;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityDamageEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final DamageCauseHolder damageCause;
    private double damage;
}
