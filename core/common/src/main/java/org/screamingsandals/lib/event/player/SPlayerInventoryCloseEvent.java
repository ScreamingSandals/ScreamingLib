package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface SPlayerInventoryCloseEvent extends SPlayerEvent, PlatformEventWrapper {

    Container getTopInventory();

    Container getBottomInventory();

    // TODO: holder?
    @LimitedVersionSupport("Paper")
    NamespacedMappingKey getReason();
}
