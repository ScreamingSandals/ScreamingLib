package org.screamingsandals.lib.sponge.material;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeMaterialMapping extends MaterialMapping implements SpongeRegistryMapper<ItemType> {
    public static void init() {
        MaterialMapping.init(SpongeMaterialMapping::new);
    }

    public SpongeMaterialMapping() {
        if (Sponge.getPlatform().getMinecraftVersion().isLegacy()) {
            platform = Platform.JAVA_LEGACY;
        } else {
            platform = Platform.JAVA_FLATTENING;
        }

        materialConverter
                .registerW2P(ItemType.class, holder -> getEntry(holder.getPlatformName()).value())
                .registerW2P(ItemStack.class, holder -> {
                    var stack = ItemStack.of(getEntry(holder.getPlatformName()).value(), (byte) 1);
                    stack.offer(Keys.ITEM_DURABILITY, holder.getDurability());
                    return stack;
                })
                .registerP2W(ItemType.class, material -> new MaterialHolder(getKeyByValue(material).getFormatted()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(getKeyByValue(stack.getType()).getFormatted(), stack.getOrElse(Keys.ITEM_DURABILITY, 0)));

        getAllKeys().forEach(key ->
                mapping.put(NamespacedMappingKey.of(key.getFormatted()), new MaterialHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<ItemType> getRegistryType() {
        return RegistryTypes.ITEM_TYPE;
    }
}
