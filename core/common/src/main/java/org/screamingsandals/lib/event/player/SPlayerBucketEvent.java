package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerBucketEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockHolder> blockClicked;
    private final ImmutableObjectLink<BlockFace> blockFace;
    private final ImmutableObjectLink<MaterialHolder> bucket;
    private final ObjectLink<Item> item;
    private final ImmutableObjectLink<Action> action;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockHolder getBlock() {
        return block.get();
    }

    public BlockHolder getBlockClicked() {
        return blockClicked.get();
    }

    public BlockFace getBlockFace() {
        return blockFace.get();
    }

    public MaterialHolder getBucket() {
        return bucket.get();
    }

    public Item getItem() {
        return item.get();
    }

    public void setItem(Item item) {
        this.item.set(item);
    }

    public Action getAction() {
        return action.get();
    }

    public enum Action {
        EMPTY,
        FILL
    }
}
