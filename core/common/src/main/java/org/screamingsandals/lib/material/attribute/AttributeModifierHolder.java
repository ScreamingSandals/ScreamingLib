package org.screamingsandals.lib.material.attribute;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
public class AttributeModifierHolder implements Wrapper {
    private final UUID uuid;
    private final String name;
    private final double amount;
    private final Operation operation;

    @Override
    public <T> T as(Class<T> type) {
        return AttributeMapping.convertAttributeModifierHolder(this, type);
    }

    public enum Operation {
        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL;

        public static Operation byOrdinal(int ordinal) {
            return values()[ordinal];
        }
    }
}
