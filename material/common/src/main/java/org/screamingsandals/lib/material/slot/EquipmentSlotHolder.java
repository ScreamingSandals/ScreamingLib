package org.screamingsandals.lib.material.slot;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

@Data
public class EquipmentSlotHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return EquipmentSlotMapping.convertEquipmentSlotHolder(this, type);
    }
}
