package org.screamingsandals.lib.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityExplodeEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final LocationHolder location;
    private final List<BlockHolder> blocks;
    private float yield;
}