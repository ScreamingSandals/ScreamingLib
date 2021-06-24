package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = true)
public class SEntityCombustByBlockEvent extends SEntityCombustEvent {
    public SEntityCombustByBlockEvent(EntityBasic entity, int duration) {
        super(entity, duration);
    }
}
