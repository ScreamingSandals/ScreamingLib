package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.block.BlockHolder;

@LimitedVersionSupport(">= 1.17")
public interface SSculkSensorReceiveEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    @Nullable
    EntityBasic getEntity();

    /**
     * TODO: Create game event mapping
     */
    NamespacedMappingKey getUnderlyingEvent();
}
