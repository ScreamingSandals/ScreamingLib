package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;

public interface SEntityCombustByEntityEvent extends SEntityCombustEvent {

    EntityBasic getCombuster();
}
