package org.screamingsandals.lib.material.slot;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EquipmentSlotMapping {
    private static EquipmentSlotMapping equipmentSlotMapping;

    protected final BidirectionalConverter<EquipmentSlotHolder> equipmentSlotConverter = BidirectionalConverter.build();
    protected final Map<String, EquipmentSlotHolder> mapping = new HashMap<>();

    public static void init(Supplier<EquipmentSlotMapping> supplier) {
        if (equipmentSlotMapping != null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is already initialized.");
        }

        equipmentSlotMapping = supplier.get();
        equipmentSlotMapping.equipmentSlotConverter.finish();
        equipmentSlotMapping.legacyMapping();
    }

    public static Optional<EquipmentSlotHolder> resolve(Object slot) {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }

        if (slot == null) {
            return Optional.empty();
        }

        if (equipmentSlotMapping.mapping.containsKey(slot.toString().toUpperCase())) {
            return Optional.of(equipmentSlotMapping.mapping.get((slot.toString().toUpperCase())));
        }

        return Optional.empty();
    }

    public static <T> T convertEquipmentSlotHolder(EquipmentSlotHolder holder, Class<T> newType) {
        if (equipmentSlotMapping == null) {
            throw new UnsupportedOperationException("EquipmentSlotMapping is not initialized yet.");
        }
        return equipmentSlotMapping.equipmentSlotConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return equipmentSlotMapping != null;
    }

    private void legacyMapping() {
        // Vanilla <-> Bukkit
        f2l("MAIN_HAND", "HAND");
        f2l("OFF_HAND", "OFF_HAND");
        f2l("BOOTS", "FEET");
        f2l("LEGGINGS", "LEGS");
        f2l("CHESTPLATE", "CHEST");
        f2l("HELMET", "HEAD");
    }

    private void f2l(String slot1, String slot2) {
        if (slot1 == null || slot2 == null) {
            throw new IllegalArgumentException("Both slots mustn't be null!");
        }

        if (mapping.containsKey(slot1) && !mapping.containsKey(slot2)) {
            mapping.put(slot2, mapping.get(slot1));
        } else if (mapping.containsKey(slot2) && !mapping.containsKey(slot1)) {
            mapping.put(slot1, mapping.get(slot2));
        }
    }
}
