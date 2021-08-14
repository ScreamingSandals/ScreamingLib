package org.screamingsandals.lib.material.slot;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Optional;

@Data
public class EquipmentSlotHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return EquipmentSlotMapping.convertEquipmentSlotHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    public static EquipmentSlotHolder of(Object slot) {
        return ofOptional(slot).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    public static Optional<EquipmentSlotHolder> ofOptional(Object slot) {
        if (slot instanceof EquipmentSlotHolder) {
            return Optional.of((EquipmentSlotHolder) slot);
        }
        return EquipmentSlotMapping.resolve(slot);
    }
}
