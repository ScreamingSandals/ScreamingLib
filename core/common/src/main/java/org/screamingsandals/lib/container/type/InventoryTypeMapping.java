package org.screamingsandals.lib.container.type;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
// TODO: Bukkit's inventory types doesn't exactly match the vanilla types
public abstract class InventoryTypeMapping extends AbstractTypeMapper<InventoryTypeHolder> {
    private static InventoryTypeMapping inventoryTypeMapping;

    protected final BidirectionalConverter<InventoryTypeHolder> inventoryTypeConverter = BidirectionalConverter.<InventoryTypeHolder>build()
            .registerP2W(InventoryTypeHolder.class, e -> e);

    protected InventoryTypeMapping() {
        if (inventoryTypeMapping != null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is already initialized.");
        }
        inventoryTypeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.INVENTORY_TYPE)
    @OfMethodAlternative(value = InventoryTypeHolder.class, methodName = "ofOptional")
    public static Optional<InventoryTypeHolder> resolve(Object entity) {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }

        if (entity == null) {
            return Optional.empty();
        }

        return inventoryTypeMapping.inventoryTypeConverter.convertOptional(entity).or(() -> inventoryTypeMapping.resolveFromMapping(entity));
    }

    @OfMethodAlternative(value = InventoryTypeHolder.class, methodName = "all")
    public static List<InventoryTypeHolder> getValues() {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(inventoryTypeMapping.values);
    }

    public static <T> T convertInventoryTypeHolder(InventoryTypeHolder holder, Class<T> newType) {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }
        return inventoryTypeMapping.inventoryTypeConverter.convert(holder, newType);
    }

    public static int getSize(InventoryTypeHolder holder) {
        if (inventoryTypeMapping == null) {
            throw new UnsupportedOperationException("InventoryTypeMapping is not initialized yet.");
        }
        return inventoryTypeMapping.getSize0(holder);
    }

    public abstract int getSize0(InventoryTypeHolder holder);
}
