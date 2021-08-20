package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerHarvestBlockEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<BlockHolder> harvestedBlock;
    private final Collection<Item> itemsHarvested;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockHolder getHarvestedBlock() {
        return harvestedBlock.get();
    }
}
