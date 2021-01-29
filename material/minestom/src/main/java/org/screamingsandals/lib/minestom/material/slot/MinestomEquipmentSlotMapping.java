package org.screamingsandals.lib.minestom.material.slot;

import net.minestom.server.network.packet.server.play.EntityEquipmentPacket;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Arrays;

@Service
public class MinestomEquipmentSlotMapping extends EquipmentSlotMapping {
    public static void init() {
        EquipmentSlotMapping.init(MinestomEquipmentSlotMapping::new);
    }

    public MinestomEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(EntityEquipmentPacket.Slot.class, equipmentSlot -> new EquipmentSlotHolder(equipmentSlot.name()))
                .registerW2P(EntityEquipmentPacket.Slot.class, equipmentSlotHolder -> EntityEquipmentPacket.Slot.valueOf(equipmentSlotHolder.getPlatformName()));

        Arrays.stream(EntityEquipmentPacket.Slot.values()).forEach(equipmentSlot -> mapping.put(equipmentSlot.name(), new EquipmentSlotHolder(equipmentSlot.name())));
    }
}
