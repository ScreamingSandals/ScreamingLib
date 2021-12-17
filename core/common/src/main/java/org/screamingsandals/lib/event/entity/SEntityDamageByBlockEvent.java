package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;

public interface SEntityDamageByBlockEvent extends SEntityDamageEvent {
    @Nullable
    BlockHolder getDamager();
}
