/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.item;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.item.builder.BukkitItemBuilder;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.stream.Collectors;

public class BukkitItem extends BasicWrapper<ItemStack> implements Item {
    public BukkitItem(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ItemTypeHolder getType() {
        return ItemTypeHolder.of(wrappedObject.getType());
    }

    @Override
    public int getAmount() {
        return wrappedObject.getAmount();
    }

    @Override
    public @Nullable Component getDisplayName() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            if (BukkitCore.getSpectatorBackend().hasAdventure()) {
                return AdventureBackend.wrapComponent(meta.displayName());
            } else {
                return Component.fromLegacy(meta.getDisplayName());
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Component> getLore() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasLore()) {
            if (BukkitCore.getSpectatorBackend().hasAdventure()) {
                return Objects.requireNonNullElseGet(meta.lore(), List::<net.kyori.adventure.text.Component>of)
                        .stream()
                        .map(AdventureBackend::wrapComponent)
                        .collect(Collectors.toUnmodifiableList());
            } else {
                return Objects.requireNonNullElseGet(meta.getLore(), List::<String>of)
                        .stream()
                        .map(Component::fromLegacy)
                        .collect(Collectors.toUnmodifiableList());
            }
        }
        return List.of();
    }

    @Override
    public List<ItemAttributeHolder> getAttributeModifiers() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            // TODO: find solution: missing Bukkit API for older versions
            if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                if (meta.hasAttributeModifiers()) {
                    var list = new ArrayList<ItemAttributeHolder>();
                    meta.getAttributeModifiers()
                            .forEach((attribute, attributeModifier) ->
                                    AttributeMapping.wrapItemAttribute(new BukkitItemAttribute(attribute, attributeModifier))
                                            .ifPresent(list::add)
                            );
                    return list;
                }
            }
        }
        return List.of();
    }

    @Override
    public List<EnchantmentHolder> getEnchantments() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            var list = new ArrayList<EnchantmentHolder>();
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).getStoredEnchants().entrySet().forEach(entry ->
                        EnchantmentHolder.ofOptional(entry).ifPresent(list::add)
                );
            } else {
                meta.getEnchants().entrySet().forEach(entry ->
                        EnchantmentHolder.ofOptional(entry).ifPresent(list::add)
                );
            }
            return list;
        }
        return List.of();
    }

    @Override
    public ItemData getData() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "getPersistentDataContainer")) { // 1.14+
                return new BukkitItemDataPersistentContainer(BukkitCore.getPlugin(), meta.getPersistentDataContainer());
            } else if (Reflect.hasMethod(meta, "getCustomTagContainer")) { // 1.13.2
                return new BukkitItemDataCustomTags(BukkitCore.getPlugin(), meta.getCustomTagContainer());
            } else {
                var unhandled = (Map<String, Object>) Reflect.getField(meta, "unhandledTags");
                if (unhandled.containsKey("PublicBukkitValues")) {
                    // TODO: check if this rly copies the tags and not just reference
                    var compound = unhandled.get("PublicBukkitValues");
                    var nmap = new HashMap<String, Object>();
                    if (CompoundTagAccessor.getType().isInstance(compound)) {
                        var keys = (Set) Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodGetAllKeys1());
                        for (var key : keys) {
                            nmap.put(key.toString(), Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodGet1(), key));
                        }
                    }
                    return new CraftBukkitItemData(nmap);
                } else {
                    return new CraftBukkitItemData(new HashMap<>());
                }
            }
        }
        return null;
    }

    @Override
    public List<HideFlags> getHideFlags() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return meta.getItemFlags().stream().map(ItemFlag::name).map(HideFlags::convert).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Integer getCustomModelData() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            try {
                if (meta.hasCustomModelData()) {
                    return meta.getCustomModelData();
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    @Override
    public boolean isUnbreakable() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "isUnbreakable")) {
                return meta.isUnbreakable();
            } else {
                var spigot = Reflect.fastInvoke(meta, "spigot");
                if (spigot != null) {
                    return (boolean) Reflect.fastInvoke(spigot, "isUnbreakable");
                }
            }
        }
        return false;
    }

    @Override
    public int getRepairCost() {
        var meta = wrappedObject.getItemMeta();
        if (meta instanceof Repairable) {
            return ((Repairable) meta).getRepairCost();
        }
        return 0;
    }

    @Override
    public ItemBuilder builder() {
        return new BukkitItemBuilder(wrappedObject.clone());
    }

    @Override
    public boolean isSimilar(Item item) {
        return wrappedObject.isSimilar(item.as(ItemStack.class));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Item clone() {
        return new BukkitItem(wrappedObject.clone());
    }

    @Override
    public boolean supportsMetadata(MetadataKey<?> key) {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return ItemMetaHelper.supportsMetadata(meta, key);
        }
        return false;
    }

    @Override
    public boolean supportsMetadata(MetadataCollectionKey<?> key) {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return ItemMetaHelper.supportsMetadata(meta, key);
        }
        return false;
    }

    @Override
    @Nullable
    public <T> T getMetadata(MetadataKey<T> key) {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return ItemMetaHelper.getMetadata(meta, key);
        }
        return null;
    }

    @Override
    public <T> Optional<T> getMetadataOptional(MetadataKey<T> key) {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return Optional.ofNullable(ItemMetaHelper.getMetadata(meta, key));
        }
        return Optional.empty();
    }

    @Override
    @Nullable
    public <T> Collection<T> getMetadata(MetadataCollectionKey<T> key) {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return ItemMetaHelper.getMetadata(meta, key);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return ItemFactory.convertItem(this, type);
        }
    }
}
