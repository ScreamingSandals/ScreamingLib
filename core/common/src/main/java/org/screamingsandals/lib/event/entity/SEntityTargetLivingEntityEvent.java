package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SEntityTargetLivingEntityEvent extends SEntityTargetEvent {
    public SEntityTargetLivingEntityEvent(ImmutableObjectLink<EntityBasic> entity, ObjectLink<EntityBasic> target, ImmutableObjectLink<TargetReason> targetReason) {
        super(entity, target, targetReason);
    }
}
