package org.screamingsandals.lib.bukkit.slot;

import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Arrays;

@Service
public class BukkitEquipmentSlotMapping extends EquipmentSlotMapping {
    public BukkitEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(EquipmentSlot.class, equipmentSlot -> new EquipmentSlotHolder(equipmentSlot.name()))
                .registerW2P(EquipmentSlot.class, equipmentSlotHolder -> EquipmentSlot.valueOf(equipmentSlotHolder.getPlatformName()));

        Arrays.stream(EquipmentSlot.values()).forEach(equipmentSlot -> mapping.put(equipmentSlot.name(), new EquipmentSlotHolder(equipmentSlot.name())));
    }
}
