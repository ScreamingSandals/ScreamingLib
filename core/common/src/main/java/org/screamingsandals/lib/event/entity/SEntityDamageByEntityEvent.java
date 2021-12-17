package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
public interface SEntityDamageByEntityEvent extends SEntityDamageEvent {
    EntityBasic getDamager();
}
