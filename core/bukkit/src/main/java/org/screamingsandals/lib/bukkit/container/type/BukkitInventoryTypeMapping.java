package org.screamingsandals.lib.bukkit.container.type;

import org.bukkit.event.inventory.InventoryType;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.container.type.InventoryTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitInventoryTypeMapping extends InventoryTypeMapping {
    public BukkitInventoryTypeMapping() {
        inventoryTypeConverter
                .registerP2W(InventoryType.class, inventoryType -> new InventoryTypeHolder(inventoryType.name()))
                .registerW2P(InventoryType.class, inventoryTypeHolder -> InventoryType.valueOf(inventoryTypeHolder.getPlatformName()));

        Arrays.stream(InventoryType.values()).forEach(inventoryType -> mapping.put(NamespacedMappingKey.of(inventoryType.name()), new InventoryTypeHolder(inventoryType.name())));
    }

    @Override
    public int getSize0(InventoryTypeHolder holder) {
        return holder.as(InventoryType.class).getDefaultSize();
    }
}
