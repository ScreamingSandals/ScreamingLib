package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerHarvestBlockEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final BlockHolder harvestedBlock;
    private final List<Item> itemsHarvested;
}
