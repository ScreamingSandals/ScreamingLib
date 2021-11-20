package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerInventoryOpenEvent extends SCancellableEvent, SPlayerEvent {

    Container getTopInventory();

    Container getBottomInventory();
}
