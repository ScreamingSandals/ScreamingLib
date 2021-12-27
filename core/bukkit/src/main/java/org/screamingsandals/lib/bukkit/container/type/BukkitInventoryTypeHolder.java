package org.screamingsandals.lib.bukkit.container.type;

import org.bukkit.event.inventory.InventoryType;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitInventoryTypeHolder extends BasicWrapper<InventoryType> implements InventoryTypeHolder {

    protected BukkitInventoryTypeHolder(InventoryType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int size() {
        return wrappedObject.getDefaultSize();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof InventoryType || object instanceof InventoryTypeHolder) {
            return equals(object);
        }
        return equals(InventoryTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
