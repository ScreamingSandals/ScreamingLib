package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomItemTypeMapper extends ItemTypeMapper {
    public MinestomItemTypeMapper() {
        itemTypeConverter
                .registerP2W(Material.class, MinestomItemTypeHolder::new)
                .registerP2W(ItemStack.class, stack -> new MinestomItemTypeHolder(stack.getMaterial()));

        Material.values().stream()
                .filter(material -> !material.isBlock())
                .forEach(material -> {
                    final var holder = new MinestomItemTypeHolder(material);
                    mapping.put(NamespacedMappingKey.of(material.name()), holder);
                    values.add(holder);
                });
    }
}
