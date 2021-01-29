package org.screamingsandals.lib.material.attribute;

import lombok.Data;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
public class AttributeModifierHolder implements Wrapper {
    private final UUID uuid;
    private final String name;
    private final double amount;
    private final Operation operation;
    private final EquipmentSlotHolder slot;

    @Override
    public <T> T as(Class<T> type) {
        return AttributeMapping.convertAttributeModifierHolder(this, type);
    }

    public enum Operation {
        /**
         * Adds (or subtracts) the specified amount to the base value.
         */
        ADD_NUMBER,
        /**
         * Adds this scalar of amount to the base value.
         */
        ADD_SCALAR,
        /**
         * Multiply amount by this value, after adding 1 to it.
         */
        MULTIPLY_SCALAR_1;
    }
}
