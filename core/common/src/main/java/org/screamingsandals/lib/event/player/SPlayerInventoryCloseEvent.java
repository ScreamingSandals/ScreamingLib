package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPlayerInventoryCloseEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Container> topInventory;
    private final ImmutableObjectLink<Container> bottomInventory;
    // TODO: holder?
    private final ImmutableObjectLink<NamespacedMappingKey> reason;

    public PlayerWrapper getPlayer() {
        return player.get();
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
