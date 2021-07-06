package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockIgniteEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final IgniteCause igniteCause;
    @Nullable
    private final BlockHolder ignitingBlock;
    @Nullable
    private final EntityBasic ignitingEntity;

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
