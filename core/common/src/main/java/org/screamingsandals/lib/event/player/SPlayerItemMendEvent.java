package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerItemMendEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final Item item;
    //TODO:
    private final Object experienceOrb;
    private int repairAmount;
}
