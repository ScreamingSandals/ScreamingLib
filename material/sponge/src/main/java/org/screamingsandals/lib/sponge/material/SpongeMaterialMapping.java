package org.screamingsandals.lib.sponge.material;

import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AutoInitialization;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;

@AutoInitialization(platform = PlatformType.SPONGE)
public class SpongeMaterialMapping extends MaterialMapping {
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
                .registerW2P(ItemType.class, holder -> Sponge.getGame().registries().registry(RegistryTypes.ITEM_TYPE).findEntry(ResourceKey.resolve(holder.getPlatformName())).orElseThrow().value())
                .registerW2P(ItemStack.class, holder -> {
                    var stack = ItemStack.of(Sponge.getGame().registries().registry(RegistryTypes.ITEM_TYPE).findEntry(ResourceKey.resolve(holder.getPlatformName())).orElseThrow().value(), (byte) 1);
                    stack.offer(Keys.ITEM_DURABILITY, holder.getDurability());
                    return stack;
                })
                .registerP2W(ItemType.class, material -> new MaterialHolder(Sponge.getGame().registries().registry(RegistryTypes.ITEM_TYPE).findValueKey(material).orElseThrow().getFormatted()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(Sponge.getGame().registries().registry(RegistryTypes.ITEM_TYPE).findValueKey(stack.getType()).orElseThrow().getFormatted(), stack.getOrElse(Keys.ITEM_DURABILITY, 0)));

        Sponge.getGame().registries().registry(RegistryTypes.ITEM_TYPE).forEach(itemType ->
            materialMapping.put(itemType.key().getNamespace().equals(ResourceKey.MINECRAFT_NAMESPACE) ? itemType.key().getValue().toUpperCase() : itemType.key().getFormatted().toUpperCase(), new MaterialHolder(itemType.key().getFormatted()))
        );
    }
}
