package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Optional;

@Service
public class BukkitItemTypeMapper extends ItemTypeMapper {
    public BukkitItemTypeMapper() {
        itemTypeConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.platformName()))
                .registerW2P(ItemStack.class, holder -> {
                    if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_FLATTENING) {
                        ItemStack stack = new ItemStack(Material.valueOf(holder.platformName()));
                        ItemMeta meta = stack.getItemMeta();
                        if (meta instanceof Damageable) {
                            ((Damageable) meta).setDamage(holder.durability());
                            stack.setItemMeta(meta);
                        }
                        return stack;
                    } else if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
                        return new ItemStack(Material.valueOf(holder.platformName()), 1, holder.durability());
                    } else {
                        throw new UnsupportedOperationException("Unknown platform!");
                    }
                })
                .registerP2W(Material.class, material -> new ItemTypeHolder(material.name()))
                .registerP2W(ItemStack.class, stack -> new ItemTypeHolder(stack.getType().name(), stack.getDurability()));

        Arrays.stream(Material.values())
                .filter(t -> !t.name().startsWith("LEGACY"))
                .filter(material -> {
                    try {
                        return material.isItem();
                    } catch (Throwable ignored) {
                        return true; // we are on older versions and yes, it is probably item xdd
                    }
                })
                .forEach(material -> {
                    var holder = new ItemTypeHolder(material.name());
                    mapping.put(NamespacedMappingKey.of(material.name()), holder);
                    values.add(holder);
                });
    }

    @Override
    protected Optional<BlockTypeHolder> getBlock0(ItemTypeHolder typeHolder) {
        var material = typeHolder.as(Material.class);
        if (!material.isBlock()) {
            return Optional.empty();
        }
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            return Optional.of(BlockTypeHolder.of(material).withLegacyData((byte) typeHolder.durability()));
        } else {
            return Optional.of(BlockTypeHolder.of(material));
        }
    }

    @Override
    protected int getMaxStackSize0(ItemTypeHolder materialHolder) {
        return materialHolder.as(Material.class).getMaxStackSize();
    }
}
