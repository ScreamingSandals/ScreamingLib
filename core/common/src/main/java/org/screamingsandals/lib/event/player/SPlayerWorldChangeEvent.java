package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.world.WorldHolder;

public interface SPlayerWorldChangeEvent extends SPlayerEvent {

    WorldHolder getFrom();
}
