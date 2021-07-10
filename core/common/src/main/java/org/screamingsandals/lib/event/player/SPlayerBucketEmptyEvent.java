package org.screamingsandals.lib.event.player;


import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

public class SPlayerBucketEmptyEvent extends SPlayerBucketEvent {

    public SPlayerBucketEmptyEvent(@NotNull final PlayerWrapper player, @NotNull final BlockHolder block,
                                   @NotNull final BlockHolder blockClicked, @NotNull final BlockFace blockFace,
                                   @NotNull final MaterialHolder bucket, @NotNull final Item itemInHand) {
        super(player, block, blockClicked, blockFace, bucket, itemInHand);
    }

}