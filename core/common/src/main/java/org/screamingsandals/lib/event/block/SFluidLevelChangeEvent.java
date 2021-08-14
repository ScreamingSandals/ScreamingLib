package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport(">= 1.13.2")
public class SFluidLevelChangeEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final BlockDataHolder newBlockData;
}
