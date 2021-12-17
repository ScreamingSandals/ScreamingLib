package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerBucketEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    BlockHolder getBlockClicked();

    BlockFace getBlockFace();

    ItemTypeHolder getBucket();

    @Nullable
    Item getItem();

    void setItem(@Nullable Item item);

    Action getAction();

    enum Action {
        EMPTY,
        FILL
    }
}
