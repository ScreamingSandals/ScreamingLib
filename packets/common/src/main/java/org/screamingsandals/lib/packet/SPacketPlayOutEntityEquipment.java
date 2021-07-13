package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.material.Item;

public interface SPacketPlayOutEntityEquipment extends SPacket {

    SPacketPlayOutEntityEquipment setEntityId(int entityId);

    SPacketPlayOutEntityEquipment setItemAndSlot(Item item, Slot slot);

    enum Slot {
        HEAD,
        OFF_HAND,
        HAND,
        CHEST,
        LEGS,
        FEET
    }
}
