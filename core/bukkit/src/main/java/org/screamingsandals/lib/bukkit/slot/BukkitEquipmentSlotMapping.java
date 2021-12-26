package org.screamingsandals.lib.bukkit.slot;

import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEquipmentSlotMapping extends EquipmentSlotMapping {
    public BukkitEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(EquipmentSlot.class, BukkitEquipmentSlotHolder::new);

        Arrays.stream(EquipmentSlot.values()).forEach(equipmentSlot -> {
            var holder = new BukkitEquipmentSlotHolder(equipmentSlot);
            mapping.put(NamespacedMappingKey.of(equipmentSlot.name()), holder);
            values.add(holder);
        });
    }
}
