package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.material.Item;

import java.util.concurrent.TimeUnit;

public interface EntityItem extends EntityBasic {

    Item getItem();

    void setItem(Item stack);

    int getPickupDelay();

    /**
     * Sets the pickup delay.
     *
     * @param delay
     * @param timeUnit
     */
    void setPickupDelay(int delay, TimeUnit timeUnit);

    boolean isPickable();

    void setPickable(boolean pickable);

    boolean isMergeable();

    void setMergeable(boolean mergeable);

    float getMergeRange();

    void setMergeRange(float mergeRange);

    long getSpawnTime();
}
