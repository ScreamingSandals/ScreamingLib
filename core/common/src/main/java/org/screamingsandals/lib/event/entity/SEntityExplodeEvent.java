package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Collection;

public interface SEntityExplodeEvent extends SCancellableEvent {

    EntityBasic getEntity();

    LocationHolder getLocation();

    Collection<BlockHolder> getBlocks();

    float getYield();

    void setYield(float yield);
}
