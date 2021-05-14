package org.screamingsandals.lib.common;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.Item;

public interface SPacketPlayOutEntityEquipment {

    void setEntity(EntityBasic entity);

    void setItemAndSlot(Item item, Slot slot);

    enum Slot {
        HEAD,
        OFF_HAND,
        HAND,
        CHEST,
        LEGS,
        FEET
    }
}
