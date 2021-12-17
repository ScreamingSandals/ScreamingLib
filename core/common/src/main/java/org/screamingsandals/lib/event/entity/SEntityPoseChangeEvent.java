package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;

public interface SEntityPoseChangeEvent extends SEvent, PlatformEventWrapper {
    EntityBasic getEntity();

    EntityPoseHolder getPose();
}
