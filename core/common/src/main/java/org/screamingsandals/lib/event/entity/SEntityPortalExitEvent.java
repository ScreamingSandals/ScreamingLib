package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

public interface SEntityPortalExitEvent extends SEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    LocationHolder getFrom();

    void setFrom(LocationHolder location);

    LocationHolder getTo();

    void setTo(LocationHolder location);

    Vector3D getBefore();

    Vector3D getAfter();

    void setAfter(Vector3D after);
}
