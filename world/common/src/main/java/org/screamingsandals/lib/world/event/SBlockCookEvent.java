package org.screamingsandals.lib.world.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockCookEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final Item source;
    private final Item result;
}
