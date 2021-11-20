package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockDispenseEvent extends SCancellableEvent {

    BlockHolder getBlock();

    Item getItem();

    void setItem(Item item);

    Vector3D getVelocity();

    void setVelocity(Vector3D velocity);

    /*
     * Only in case when dispenser is used to attach armor to an entity
     */
    @Nullable
    EntityLiving getReceiver();
}
