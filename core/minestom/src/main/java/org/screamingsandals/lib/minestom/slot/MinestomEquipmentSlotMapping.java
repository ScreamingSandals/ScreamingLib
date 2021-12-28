package org.screamingsandals.lib.minestom.slot;

import net.minestom.server.item.attribute.AttributeSlot;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomEquipmentSlotMapping extends EquipmentSlotMapping {
    public MinestomEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(AttributeSlot.class, attributeSlot -> new EquipmentSlotHolder(attributeSlot.name()))
                .registerW2P(AttributeSlot.class, equipmentSlotHolder -> AttributeSlot.valueOf(equipmentSlotHolder.getPlatformName()));

        Arrays.stream(AttributeSlot.values()).forEach(equipmentSlot -> {
            final var holder = new EquipmentSlotHolder(equipmentSlot.name());
            mapping.put(NamespacedMappingKey.of(equipmentSlot.name()), holder);
            values.add(holder);
        });
    }
}
