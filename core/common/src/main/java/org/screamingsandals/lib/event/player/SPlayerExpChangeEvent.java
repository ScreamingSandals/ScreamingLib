package org.screamingsandals.lib.event.player;
public interface SPlayerExpChangeEvent extends SPlayerEvent {

    int getExp();

    void setExp(int exp);
}
