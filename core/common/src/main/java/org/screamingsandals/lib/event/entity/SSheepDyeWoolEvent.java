package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SSheepDyeWoolEvent extends SCancellableEvent {

    EntityBasic getEntity();

    //TODO: DyeColor holder
    String getDyeColor();

    void setDyeColor(String dyeColor);
}
