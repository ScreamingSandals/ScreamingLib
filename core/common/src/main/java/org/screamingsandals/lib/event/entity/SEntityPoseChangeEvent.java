package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.event.AbstractEvent;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPoseChangeEvent extends AbstractEvent {
    private final EntityBasic entity;
    private final EntityPoseHolder pose;
}
