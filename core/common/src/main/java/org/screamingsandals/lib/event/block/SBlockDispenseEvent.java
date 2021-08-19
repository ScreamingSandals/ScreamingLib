package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockDispenseEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<Item> item;
    private final ImmutableObjectLink<Vector3D> velocity;
    private final ImmutableObjectLink<@Nullable EntityLiving> receiver;

    public BlockHolder getBlock() {
        return block.get();
    }

    public Item getItem() {
        return item.get();
    }

    public Vector3D getVelocity() {
        return velocity.get();
    }

    /*
     * Only in case when dispenser is used to attach armor on entity
     */
    @Nullable
    public EntityLiving getReceiver() {
        return receiver.get();
    }
}
