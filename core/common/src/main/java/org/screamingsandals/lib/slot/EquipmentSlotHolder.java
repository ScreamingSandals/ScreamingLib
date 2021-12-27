package org.screamingsandals.lib.slot;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EquipmentSlotHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static EquipmentSlotHolder of(Object slot) {
        return ofOptional(slot).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static Optional<EquipmentSlotHolder> ofOptional(Object slot) {
        if (slot instanceof EquipmentSlotHolder) {
            return Optional.of((EquipmentSlotHolder) slot);
        }
        return EquipmentSlotMapping.resolve(slot);
    }

    static List<EquipmentSlotHolder> all() {
        return EquipmentSlotMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object... objects);
}
