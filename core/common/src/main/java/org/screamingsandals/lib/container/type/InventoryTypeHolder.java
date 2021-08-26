package org.screamingsandals.lib.container.type;

import lombok.Data;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class InventoryTypeHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return InventoryTypeMapping.convertInventoryTypeHolder(this, type);
    }

    public int getSize() {
        return InventoryTypeMapping.getSize(this);
    }

    public <C extends Container> Optional<C> createContainer(Component name) {
        return ItemFactory.createContainer(this, name);
    }

    /**
     * Compares the entity type and the object
     *
     * @param object Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    /**
     * Compares the entity type and the objects
     *
     * @param objects Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    public static InventoryTypeHolder of(Object inventoryType) {
        return ofOptional(inventoryType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    public static Optional<InventoryTypeHolder> ofOptional(Object inventoryType) {
        if (inventoryType instanceof InventoryTypeHolder) {
            return Optional.of((InventoryTypeHolder) inventoryType);
        }
        return InventoryTypeMapping.resolve(inventoryType);
    }
}
