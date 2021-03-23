package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = true)
public class SEntityTargetLivingEntityEvent extends SEntityTargetEvent {
    public SEntityTargetLivingEntityEvent(EntityBasic entity, EntityBasic target, TargetReason targetReason) {
        super(entity, target, targetReason);
    }
}
