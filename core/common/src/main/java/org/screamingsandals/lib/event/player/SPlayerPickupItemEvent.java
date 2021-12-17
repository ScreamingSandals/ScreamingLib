package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.entity.SEntityPickupItemEvent;

// TODO: Is it better to handle PlayerPickupItemEvent or just handle EntityPickupItemEvent and check if the entity is instance of Player?

public interface SPlayerPickupItemEvent extends SEntityPickupItemEvent, SPlayerEvent {

}
