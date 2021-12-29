package org.screamingsandals.lib.minestom.slot;

import net.minestom.server.item.attribute.AttributeSlot;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Locale;

@Service
public class MinestomEquipmentSlotMapping extends EquipmentSlotMapping {
    public MinestomEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(AttributeSlot.class, MinestomEquipmentSlotHolder::new)
                .registerW2P(AttributeSlot.class, equipmentSlotHolder -> AttributeSlot.valueOf(equipmentSlotHolder.platformName()));

        Arrays.stream(AttributeSlot.values()).forEach(equipmentSlot -> {
            final var holder = new MinestomEquipmentSlotHolder(equipmentSlot);
            mapping.put(NamespacedMappingKey.of(equipmentSlot.name().toLowerCase(Locale.ROOT)), holder);
            values.add(holder);
        });
    }
}
