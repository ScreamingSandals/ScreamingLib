package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockHolder;

public interface SEntityPlaceEvent extends SCancellableEvent {
    EntityBasic getEntity();

    @Nullable
    PlayerWrapper getPlayer();

    BlockHolder getBlock();

    BlockFace getBlockFace();
}
