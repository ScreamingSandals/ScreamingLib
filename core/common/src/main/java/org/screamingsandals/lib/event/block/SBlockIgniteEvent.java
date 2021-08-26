package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockIgniteEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<IgniteCause> igniteCause;
    private final ImmutableObjectLink<@Nullable BlockHolder> ignitingBlock;
    private final ImmutableObjectLink<@Nullable EntityBasic> ignitingEntity;

    public BlockHolder getBlock() {
        return block.get();
    }

    public IgniteCause getIgniteCause() {
        return igniteCause.get();
    }

    @Nullable
    public BlockHolder getIgnitingBlock() {
        return ignitingBlock.get();
    }

    @Nullable
    public EntityBasic getIgnitingEntity() {
        return ignitingEntity.get();
    }

    // TODO: holder?
    public enum IgniteCause {
        ARROW,
        ENDER_CRYSTAL,
        EXPLOSION,
        FIREBALL,
        FLINT_AND_STEEL,
        LAVA,
        LIGHTNING,
        SPREAD;
    }
}
