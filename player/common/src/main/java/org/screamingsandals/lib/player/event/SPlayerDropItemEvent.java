package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerDropItemEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final Item itemDrop;
}
