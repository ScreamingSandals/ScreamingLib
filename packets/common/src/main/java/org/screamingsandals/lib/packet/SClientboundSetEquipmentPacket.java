package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.material.Item;

public interface SClientboundSetEquipmentPacket extends SPacket {

    SClientboundSetEquipmentPacket setEntityId(int entityId);

    SClientboundSetEquipmentPacket setItemAndSlot(Item item, Slot slot);

    enum Slot {
        HEAD,
        OFF_HAND,
        HAND,
        CHEST,
        LEGS,
        FEET
    }
}
