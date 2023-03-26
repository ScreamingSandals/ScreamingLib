/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bukkit.item.builder;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.bukkit.item.ItemMetaHelper;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.bukkit.nbt.NBTVanillaSerializer;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.nms.accessors.ItemStackAccessor;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BukkitItemBuilder implements ItemStackBuilder {
    private org.bukkit.inventory.ItemStack item;

    @Override
    public @NotNull ItemStackBuilder type(@NotNull ItemTypeHolder type) {
        if (item == null) {
            item = new org.bukkit.inventory.ItemStack(Material.AIR);
        }

        this.item.setType(type.as(Material.class));
        if (type.forcedDurability() != 0) {
            durability(type.forcedDurability());
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder durability(int durability) {
        return durability((short) durability);
    }

    public ItemStackBuilder durability(short durability) {
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            this.item.setDurability(durability);
        } else {
            var meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(durability);
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder amount(int amount) {
        if (item == null) {
            return this;
        }
        item.setAmount(amount);
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder displayName(@Nullable Component displayName) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (BukkitCore.getSpectatorBackend().hasAdventure()) {
                meta.displayName(displayName != null ? displayName.as(net.kyori.adventure.text.Component.class) : null);
            } else {
                meta.setDisplayName(displayName != null ? displayName.toLegacy() : null);
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder itemLore(@Nullable List<@NotNull Component> lore) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (lore == null) {
                meta.setLore(null);
            } else {
                if (BukkitCore.getSpectatorBackend().hasAdventure()) {
                    meta.lore(lore
                            .stream()
                            .map(component -> component.as(net.kyori.adventure.text.Component.class))
                            .collect(Collectors.toList())
                    );
                } else {
                    meta.setLore(lore
                            .stream()
                            .map(Component::toLegacy)
                            .collect(Collectors.toList())
                    );
                }
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder attributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers) {
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
    public @NotNull ItemStackBuilder attributeModifier(@NotNull ItemAttributeHolder modifier) {
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
    public @NotNull ItemStackBuilder data(@NotNull ItemData data) {
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
    public @NotNull ItemStackBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags) {
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
    public @NotNull ItemStackBuilder hideFlag(@NotNull HideFlags flag) {
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
    public @NotNull ItemStackBuilder enchantments(@Nullable List<@NotNull Enchantment> enchantments) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.getEnchants().keySet().forEach(meta::removeEnchant);
            if (enchantments != null) {
                enchantments.forEach(e -> {
                    if (meta instanceof EnchantmentStorageMeta) {
                        ((EnchantmentStorageMeta) meta).addStoredEnchant(e.as(org.bukkit.enchantments.Enchantment.class), e.level(), true);
                    } else {
                        meta.addEnchant(e.as(org.bukkit.enchantments.Enchantment.class), e.level(), true);
                    }
                });
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder enchantment(@NotNull Enchantment enchantment) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment.as(org.bukkit.enchantments.Enchantment.class), enchantment.level(), true);
            } else {
                meta.addEnchant(enchantment.as(org.bukkit.enchantments.Enchantment.class), enchantment.level(), true);
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder customModelData(@Nullable Integer data) {
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
    public @NotNull ItemStackBuilder unbreakable(boolean unbreakable) {
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
    public @NotNull ItemStackBuilder repairCost(int repairCost) {
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
    public @NotNull ItemStackBuilder tag(@NotNull CompoundTag tag) {
        if (item == null) {
            item = new org.bukkit.inventory.ItemStack(Material.AIR); // shouldn't we throw error instead?
        }

        if (!ClassStorage.CB.CraftItemStack.isInstance(item)) {
            item = ClassStorage.asCBStack(item);
        }
        Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.getMethodSetTag1(), NBTVanillaSerializer.serialize(tag));
        return this;
    }

    @Override
    public @Nullable ItemStack build() {
        return item != null ? new BukkitItem(item.clone()) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull ItemStackBuilder platformMeta(Object meta) {
        if (item == null) {
            return this;
        }
        if (meta instanceof ItemMeta) {
            try {
                item.setItemMeta((ItemMeta) meta);
            } catch (Throwable ignored) {
            }
        } else if (meta instanceof Map) {
            try {
                item.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject((Map<String, ?>) meta));
            } catch (Throwable ignored) {
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder lore(@NotNull Component component) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                var list = new ArrayList<Component>();
                if (BukkitCore.getSpectatorBackend().hasAdventure()) {
                    Objects.requireNonNullElseGet(meta.lore(), List::<net.kyori.adventure.text.Component>of)
                            .forEach(o -> list.add(AdventureBackend.wrapComponent(o)));
                } else {
                    Objects.requireNonNullElseGet(meta.getLore(), List::<String>of)
                            .forEach(o -> list.add(Component.fromLegacy(o)));
                }
                list.add(component);
                itemLore(list); // meta will be saved inside the itemLore method
            }
        }
        return this;
    }

    @Override
    @Deprecated
    public boolean supportsMetadata(MetadataKey<?> key) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                return ItemMetaHelper.supportsMetadata(meta, key);
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean supportsMetadata(MetadataCollectionKey<?> key) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                return ItemMetaHelper.supportsMetadata(meta, key);
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public <T> @NotNull ItemStackBuilder setMetadata(MetadataKey<T> key, T value) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                ItemMetaHelper.setMetadata(meta, key, value);
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    @Deprecated
    public <T> @NotNull ItemStackBuilder setMetadata(MetadataCollectionKey<T> key, Collection<T> value) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                ItemMetaHelper.setMetadata(meta, key, value);
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    @Deprecated
    public <T> @NotNull ItemStackBuilder addToListMetadata(MetadataCollectionKey<T> key, T value) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta != null) {
                ItemMetaHelper.addMetadata(meta, key, value);
                item.setItemMeta(meta);
            }
        }
        return this;
    }
}
