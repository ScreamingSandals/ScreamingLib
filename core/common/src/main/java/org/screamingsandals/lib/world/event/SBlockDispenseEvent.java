package org.screamingsandals.lib.world.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockDispenseEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final Item item;
    private final Vector3D velocity;
    /*
     * Only in case when dispenser is used to attach armor on entity
     */
    @Nullable
    private final Receiver receiver;

    public interface Receiver extends Wrapper {
    }
}
