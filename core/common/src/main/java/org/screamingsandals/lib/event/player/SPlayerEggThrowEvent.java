package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;

public interface SPlayerEggThrowEvent extends SPlayerEvent {

    EntityBasic getEgg();

    boolean isHatching();

    void setHatching(boolean hatching);

    EntityTypeHolder getHatchType();

    void setHatchType(EntityTypeHolder hatchType);

    byte getNumHatches();

    void setNumHatches(byte numHatches);
}
