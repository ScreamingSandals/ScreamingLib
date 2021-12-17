package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerInventoryOpenEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    Container getTopInventory();

    Container getBottomInventory();
}
