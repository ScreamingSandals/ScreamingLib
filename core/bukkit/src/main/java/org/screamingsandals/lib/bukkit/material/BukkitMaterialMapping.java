package org.screamingsandals.lib.bukkit.material;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.lib.material.MappingFlags;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitMaterialMapping extends MaterialMapping {

    @Getter
    private int versionNumber;

    @SuppressWarnings("deprecation") //legacy versions
    public BukkitMaterialMapping() {
        String[] bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        versionNumber = 0;

        for (int i = 0; i < 2; i++) {
            versionNumber += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }

        platform = versionNumber < 113 ? Platform.JAVA_LEGACY : Platform.JAVA_FLATTENING;

        if (versionNumber < 112) {
            mappingFlags.add(MappingFlags.NO_COLORED_BEDS);
        }

        materialConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .registerW2P(ItemStack.class, holder -> {
                    if (platform == Platform.JAVA_FLATTENING) {
                        ItemStack stack = new ItemStack(Material.valueOf(holder.getPlatformName()));
                        ItemMeta meta = stack.getItemMeta();
                        if (meta instanceof Damageable) {
                            ((Damageable) meta).setDamage(holder.getDurability());
                            stack.setItemMeta(meta);
                        }
                        return stack;
                    } else if (platform == Platform.JAVA_LEGACY) {
                        return new ItemStack(Material.valueOf(holder.getPlatformName()), 1, (short) holder.getDurability());
                    } else {
                        throw new UnsupportedOperationException("Unknown platform!");
                    }
                })
                .registerP2W(Material.class, material -> new MaterialHolder(material.name()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(stack.getType().name(), stack.getDurability()));

        Arrays.stream(Material.values())
                .filter(t -> !t.name().startsWith("LEGACY"))
                .forEach(material ->
                        mapping.put(NamespacedMappingKey.of(material.name()), new MaterialHolder(material.name()))
                );
    }

    @Override
    protected boolean isBlock0(MaterialHolder materialHolder) {
        return materialHolder.as(Material.class).isBlock();
    }

    @Override
    protected boolean isItem0(MaterialHolder materialHolder) {
        try {
            return materialHolder.as(Material.class).isItem();
        } catch (Throwable ignored) {
            return true; // we are on older versions and yes, it is probably item xdd
        }
    }

    @Override
    protected int getMaxStackSize0(MaterialHolder materialHolder) {
        return materialHolder.as(Material.class).getMaxStackSize();
    }
}
