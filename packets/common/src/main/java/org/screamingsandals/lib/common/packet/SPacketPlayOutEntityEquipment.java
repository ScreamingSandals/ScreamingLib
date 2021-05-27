package org.screamingsandals.lib.common.packet;
import org.screamingsandals.lib.material.Item;

public interface SPacketPlayOutEntityEquipment extends SPacket {
    void setEntityId(int entityId);

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
