package org.screamingsandals.lib.bukkit.slot;

import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitEquipmentSlotHolder extends BasicWrapper<EquipmentSlot> implements EquipmentSlotHolder {
    protected BukkitEquipmentSlotHolder(EquipmentSlot wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof EquipmentSlot || object instanceof EquipmentSlotHolder) {
            return equals(object);
        }
        return equals(EquipmentSlotHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
