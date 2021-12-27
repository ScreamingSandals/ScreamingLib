package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
public class ItemAttributeHolder implements Wrapper {
    private final AttributeTypeHolder type;
    private final UUID uuid;
    private final String name;
    private final double amount;
    private final AttributeModifierHolder.Operation operation;
    @Nullable
    private final EquipmentSlotHolder slot;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        return AttributeMapping.convertItemAttributeHolder(this, type);
    }
}
