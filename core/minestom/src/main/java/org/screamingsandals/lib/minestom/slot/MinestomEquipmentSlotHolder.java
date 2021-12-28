package org.screamingsandals.lib.minestom.slot;

import net.minestom.server.item.attribute.AttributeSlot;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomEquipmentSlotHolder extends BasicWrapper<AttributeSlot> implements EquipmentSlotHolder {
    protected MinestomEquipmentSlotHolder(AttributeSlot wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof AttributeSlot || object instanceof EquipmentSlotHolder) {
            return equals(object);
        }
        return equals(EquipmentSlotHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
