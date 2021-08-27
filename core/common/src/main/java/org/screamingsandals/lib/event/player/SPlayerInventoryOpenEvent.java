package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SPlayerInventoryOpenEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Container> topInventory;
    private final ImmutableObjectLink<Container> bottomInventory;

    public SPlayerInventoryOpenEvent(ImmutableObjectLink<PlayerWrapper> player,
                                     ImmutableObjectLink<Container> topInventory,
                                     ImmutableObjectLink<Container> bottomInventory) {
        super(player);
        this.topInventory = topInventory;
        this.bottomInventory = bottomInventory;
    }

    public Container getTopInventory() {
        return topInventory.get();
    }

    public Container getBottomInventory() {
        return bottomInventory.get();
    }
}
