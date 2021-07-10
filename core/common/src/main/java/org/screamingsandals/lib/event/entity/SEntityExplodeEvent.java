package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityExplodeEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final LocationHolder location;
    private final Collection<BlockHolder> blocks;
    private float yield;
}
