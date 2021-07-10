package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SRedstoneEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    @Range(from = 0, to = 15)
    private final int oldCurrent;
    @Range(from = 0, to = 15)
    private int newCurrent;
}
