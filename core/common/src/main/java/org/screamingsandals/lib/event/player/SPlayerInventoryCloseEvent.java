package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@EqualsAndHashCode(callSuper = true)
public class SPlayerInventoryCloseEvent extends SPlayerEvent {
    private final ImmutableObjectLink<Container> topInventory;
    private final ImmutableObjectLink<Container> bottomInventory;
    // TODO: holder?
    private final ImmutableObjectLink<NamespacedMappingKey> reason;

    public SPlayerInventoryCloseEvent(ImmutableObjectLink<PlayerWrapper> player,
                                      ImmutableObjectLink<Container> topInventory,
                                      ImmutableObjectLink<Container> bottomInventory,
                                      ImmutableObjectLink<NamespacedMappingKey> reason) {
        super(player);
        this.topInventory = topInventory;
        this.bottomInventory = bottomInventory;
        this.reason = reason;
    }

    public Container getTopInventory() {
        return topInventory.get();
    }

    public Container getBottomInventory() {
        return bottomInventory.get();
    }

    public NamespacedMappingKey getReason() {
        return reason.get();
    }
}
