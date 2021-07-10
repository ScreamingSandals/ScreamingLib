package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SBlockExplodeEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final Collection<BlockHolder> destroyed;
    private float yield;
}
