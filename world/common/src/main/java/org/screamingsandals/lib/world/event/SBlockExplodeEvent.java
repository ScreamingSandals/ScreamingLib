package org.screamingsandals.lib.world.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SBlockExplodeEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final List<BlockHolder> destroyed;
    private float yield;
}
