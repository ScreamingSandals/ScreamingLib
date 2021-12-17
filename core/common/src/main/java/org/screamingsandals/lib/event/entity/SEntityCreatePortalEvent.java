package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

public interface SEntityCreatePortalEvent extends SCancellableEvent, PlatformEventWrapper {
    EntityBasic getEntity();

    Collection<BlockStateHolder> getBlocks();

    PortalType getPortalType();
}
