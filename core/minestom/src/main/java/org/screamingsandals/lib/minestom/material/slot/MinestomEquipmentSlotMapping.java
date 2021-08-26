package org.screamingsandals.lib.minestom.material.slot;

import net.minestom.server.item.attribute.AttributeSlot;
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Arrays;

@Service
public class MinestomEquipmentSlotMapping extends EquipmentSlotMapping {
    public static void init() {
        EquipmentSlotMapping.init(MinestomEquipmentSlotMapping::new);
    }

    public MinestomEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(AttributeSlot.class, attributeSlot -> new EquipmentSlotHolder(attributeSlot.name()))
                .registerW2P(AttributeSlot.class, equipmentSlotHolder -> AttributeSlot.valueOf(equipmentSlotHolder.getPlatformName()));

        Arrays.stream(AttributeSlot.values()).forEach(equipmentSlot -> mapping.put(equipmentSlot.name(), new EquipmentSlotHolder(equipmentSlot.name())));
    }
}
