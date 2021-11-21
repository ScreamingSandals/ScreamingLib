package org.screamingsandals.lib.event.player;

public interface SPlayerLevelChangeEvent extends SPlayerEvent {

    int getOldLevel();

    int getNewLevel();
}
