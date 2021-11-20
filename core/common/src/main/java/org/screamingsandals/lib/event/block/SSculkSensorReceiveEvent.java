package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport(">= 1.17")
public abstract class SSculkSensorReceiveEvent extends CancellableAbstractEvent {

    public abstract BlockHolder getBlock();

    @Nullable
    public abstract EntityBasic getEntity();

    /**
     * TODO: Create game event mapping
     */
    public abstract NamespacedMappingKey getUnderlyingEvent();
}
