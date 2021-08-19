package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPlaceEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<EntityBasic> player;
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockFace> blockFace;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityBasic getPlayer() {
        return player.get();
    }

    public BlockHolder getBlock() {
        return block.get();
    }

    public BlockFace getBlockFace() {
        return blockFace.get();
    }
}
