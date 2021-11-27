package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport("Bukkit >= 1.16")
public interface SStriderTemperatureChangeEvent extends SEvent {

    EntityLiving getEntity();

    boolean isShivering();
}
