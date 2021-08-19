package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport(">= 1.17")
public class SSculkSensorReceiveEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    /**
     * TODO: Create game event mapping
     */
    private final ImmutableObjectLink<NamespacedMappingKey> underlyingEvent;
    private final ImmutableObjectLink<@Nullable EntityBasic> entity;

    public BlockHolder getBlock() {
        return block.get();
    }

    @Nullable
    public EntityBasic getEntity() {
        return entity.get();
    }

    public NamespacedMappingKey getUnderlyingEvent() {
        return underlyingEvent.get();
    }
}
