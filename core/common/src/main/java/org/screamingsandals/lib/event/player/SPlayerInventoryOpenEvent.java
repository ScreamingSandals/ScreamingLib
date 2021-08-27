package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPlayerInventoryOpenEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Container> topInventory;
    private final ImmutableObjectLink<Container> bottomInventory;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Container getTopInventory() {
        return topInventory.get();
    }

    public Container getBottomInventory() {
        return bottomInventory.get();
    }
}
