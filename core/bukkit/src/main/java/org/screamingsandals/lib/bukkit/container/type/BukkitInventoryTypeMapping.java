package org.screamingsandals.lib.bukkit.container.type;

import org.bukkit.event.inventory.InventoryType;
import org.screamingsandals.lib.container.type.InventoryTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitInventoryTypeMapping extends InventoryTypeMapping {
    public BukkitInventoryTypeMapping() {
        inventoryTypeConverter
                .registerP2W(InventoryType.class, BukkitInventoryTypeHolder::new);

        Arrays.stream(InventoryType.values()).forEach(inventoryType -> {
            var holder = new BukkitInventoryTypeHolder(inventoryType);
            mapping.put(NamespacedMappingKey.of(inventoryType.name()), holder);
            values.add(holder);
        });
    }
}
