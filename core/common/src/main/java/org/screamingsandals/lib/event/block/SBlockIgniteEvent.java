package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockIgniteEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    IgniteCause getIgniteCause();

    @Nullable
    BlockHolder getIgnitingBlock();

    @Nullable
    EntityBasic getIgnitingEntity();

    // TODO: holder?
    enum IgniteCause {
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
