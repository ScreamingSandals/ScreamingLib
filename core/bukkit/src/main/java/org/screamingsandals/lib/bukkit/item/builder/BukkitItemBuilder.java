package org.screamingsandals.lib.bukkit.item.builder;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BukkitItemBuilder implements ItemBuilder {
    private ItemStack item;

    @Override
    public ItemBuilder type(@NotNull ItemTypeHolder type) {
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }

        this.item.setType(type.as(Material.class));
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            this.item.setDurability(type.durability());
        } else {
            if (type.durability() != 0) {
                var meta = item.getItemMeta();
                if (meta instanceof Damageable) {
                    ((Damageable) meta).setDamage(type.durability());
                    item.setItemMeta(meta);
                }
            }
        }
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        if (item == null) {
            return this;
        }
        item.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder displayName(@Nullable Component displayName) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            ComponentObjectLink.processSetter(meta, "displayName", meta::setDisplayName, displayName);
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public ItemBuilder lore(@Nullable List<@NotNull Component> lore) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (lore == null) {
                meta.setLore(null);
            } else {
                AdventureUtils
                        .get(meta, "lore", List.class)
                        .ifPresentOrElse(classMethod ->
                                        classMethod.invokeInstance(meta, lore
                                                .stream()
                                                .map(ComponentUtils::componentToPlatform)
                                                .collect(Collectors.toList()))
                                , () ->
                                        meta.setLore(lore
                                                .stream()
                                                .map(AdventureHelper::toLegacy)
                                                .collect(Collectors.toList()))
                        );
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public ItemBuilder attributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            // TODO: find solution: missing Bukkit API for older versions
            if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                meta.setAttributeModifiers(null);
                if (modifiers != null) {
                    modifiers.stream()
                            .map(holder -> holder.as(BukkitItemAttribute.class))
                            .forEach(holder -> meta.addAttributeModifier(holder.getAttribute(), holder.getAttributeModifier()));
                }
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public ItemBuilder attributeModifier(@NotNull ItemAttributeHolder modifier) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            // TODO: find solution: missing Bukkit API for older versions
            if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                var mod = modifier.as(BukkitItemAttribute.class);
                meta.addAttributeModifier(mod.getAttribute(), mod.getAttributeModifier());
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public ItemBuilder data(@NotNull ItemData data) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "getPersistentDataContainer")) { // 1.14+
                if (data instanceof BukkitItemDataPersistentContainer && !data.isEmpty()) {
                    var origDataContainer = ((BukkitItemDataPersistentContainer) data).getDataContainer();
                    Reflect.getMethod(meta.getPersistentDataContainer(), "putAll", Map.class)
                            .invoke(Reflect.fastInvoke(origDataContainer, "getRaw"));
                    item.setItemMeta(meta);
                }
            } else if (Reflect.hasMethod(meta, "getCustomTagContainer")) { // 1.13.2
                if (data instanceof BukkitItemDataCustomTags && !data.isEmpty()) {
                    var origDataContainer = ((BukkitItemDataCustomTags) data).getDataContainer();
                    Reflect.getMethod(meta.getCustomTagContainer(), "putAll", Map.class)
                            .invoke(Reflect.fastInvoke(origDataContainer, "getRaw"));
                    item.setItemMeta(meta);
                }
            } else {
                if (data instanceof CraftBukkitItemData && !data.isEmpty()) {
                    var unhandled = (Map<String,Object>) Reflect.getField(meta, "unhandledTags");
                    Object compound;
                    if (unhandled.containsKey("PublicBukkitValues")) {
                        compound = unhandled.get("PublicBukkitValues");
                    } else {
                        compound = Reflect.construct(CompoundTagAccessor.getConstructor0());
                        unhandled.put("PublicBukkitValues", compound);
                    }
                    ((CraftBukkitItemData) data).getKeyNBTMap().forEach((s, o) ->
                            Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodPut1(), s, o)
                    );
                    item.setItemMeta(meta);
                }
            }
        }
        return this;
    }

    @Override
    public ItemBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            try {
                meta.removeItemFlags(meta.getItemFlags().toArray(ItemFlag[]::new));
                if (flags != null) {
                    meta.addItemFlags(flags.stream().map(HideFlags::getBukkitName).map(ItemFlag::valueOf).toArray(ItemFlag[]::new));
                }

                item.setItemMeta(meta);
            } catch (Throwable ignored) {}
        }
        return this;
    }

    @Override
    public ItemBuilder hideFlag(@NotNull HideFlags flag) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            try {
                meta.addItemFlags(ItemFlag.valueOf(flag.getBukkitName()));

                item.setItemMeta(meta);
            } catch (Throwable ignored) {}
        }
        return this;
    }

    @Override
    public ItemBuilder enchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.getEnchants().keySet().forEach(meta::removeEnchant);
            if (enchantments != null) {
                enchantments.forEach(e -> {
                    if (meta instanceof EnchantmentStorageMeta) {
                        ((EnchantmentStorageMeta) meta).addStoredEnchant(e.as(Enchantment.class), e.getLevel(), true);
                    } else {
                        meta.addEnchant(e.as(Enchantment.class), e.getLevel(), true);
                    }
                });
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public ItemBuilder enchantment(@NotNull EnchantmentHolder enchantment) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment.as(Enchantment.class), enchantment.getLevel(), true);
            } else {
                meta.addEnchant(enchantment.as(Enchantment.class), enchantment.getLevel(), true);
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public ItemBuilder customModelData(@Nullable Integer data) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            try {
                meta.setCustomModelData(data);
                item.setItemMeta(meta);
            } catch (Throwable ignored) {}
        }
        return this;
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "setUnbreakable", boolean.class)) {
                meta.setUnbreakable(unbreakable);
                item.setItemMeta(meta);
            } else {
                var spigot = Reflect.fastInvoke(meta, "spigot");
                if (spigot != null) {
                    Reflect.getMethod(spigot, "setUnbreakable", boolean.class).invoke(unbreakable);
                    item.setItemMeta(meta);
                }
            }
        }
        return this;
    }

    @Override
    public ItemBuilder repairCost(int repairCost) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta instanceof Repairable) {
            ((Repairable) meta).setRepairCost(repairCost);
        }
        return this;
    }

    @Override
    public Optional<Item> build() {
        if (item != null) {
            return Optional.of(new BukkitItem(item.clone()));
        } else {
            return Optional.empty();
        }
    }
}
