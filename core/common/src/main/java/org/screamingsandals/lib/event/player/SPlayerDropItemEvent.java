package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerDropItemEvent extends SCancellableEvent, SPlayerEvent {

    EntityItem getItemDrop();
}
