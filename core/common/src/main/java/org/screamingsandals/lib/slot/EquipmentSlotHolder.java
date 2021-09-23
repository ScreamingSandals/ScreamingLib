package org.screamingsandals.lib.slot;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class EquipmentSlotHolder implements ComparableWrapper {
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

    public static List<EquipmentSlotHolder> all() {
        return EquipmentSlotMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
