package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.ProtoItemAttribute;
import org.screamingsandals.lib.utils.ProtoWrapper;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
public class ItemAttributeHolder implements Wrapper, ProtoWrapper<ProtoItemAttribute> {
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

    @Override
    public ProtoItemAttribute asProto() {
        final var builder = ProtoItemAttribute.newBuilder()
                .setType(type.platformName())
                .setUuid(uuid.toString())
                .setName(name)
                .setAmount(amount)
                .setOperation(operation.name());
        if (slot != null && slot.platformName() != null) {
            builder.setSlot(slot.platformName());
        }

        return builder.build();
    }
}
