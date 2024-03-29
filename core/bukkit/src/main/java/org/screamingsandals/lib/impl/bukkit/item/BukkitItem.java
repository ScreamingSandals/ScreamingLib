/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.item;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.item.builder.BukkitItemBuilder;
import org.screamingsandals.lib.impl.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.impl.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.impl.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.impl.bukkit.nbt.NBTVanillaSerializer;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.ItemStackView;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.NumericTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.impl.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ItemStackAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ListTagAccessor;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.StringUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.stream.Collectors;

public class BukkitItem extends BasicWrapper<org.bukkit.inventory.ItemStack> implements ItemStack {
    private @Nullable CompoundTag tagCache;

    public BukkitItem(@NotNull org.bukkit.inventory.ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ItemType getType() {
        if (BukkitFeature.FLATTENING.isSupported()) {
            return ItemType.of(wrappedObject.getType());
        } else {
            return new BukkitItemType1_8(wrappedObject.getType(), wrappedObject.getType().getMaxDurability() > 0 ? 0 : wrappedObject.getDurability()); // distinguish between durability and data value
        }
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

    @Override
    public @NotNull List<@NotNull Component> getLore() {
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
    public @NotNull List<@NotNull ItemAttribute> getAttributeModifiers() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_ATTRIBUTE_MODIFIERS_API.isSupported()) { // 1.13.1
                var bukkitModifiers = meta.getAttributeModifiers();
                if (bukkitModifiers != null && !bukkitModifiers.isEmpty()) {
                    var list = new ArrayList<ItemAttribute>(bukkitModifiers.size());
                    bukkitModifiers
                            .forEach((attribute, attributeModifier) -> {
                                var itemAttribute = Attributes.wrapItemAttribute(new BukkitItemAttribute(attribute, attributeModifier));
                                if (itemAttribute != null) {
                                    list.add(itemAttribute);
                                }
                            });
                    return list;
                }
            } else {
                // Pre 1.13.1
                Object tag;
                if (!ClassStorage.CB.CraftItemStack.isInstance(wrappedObject)) {
                    var unhandled = (Map<String, Object>) Reflect.getField(meta, "unhandledTags");
                    if (!unhandled.containsKey("AttributeModifiers")) {
                        return List.of();
                    }
                    tag = unhandled.get("AttributeModifiers");
                } else {
                    var nbt = Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(wrappedObject), ItemStackAccessor.getMethodGetTag1());
                    tag = Reflect.fastInvoke(nbt, CompoundTagAccessor.getMethodGet1(), "AttributeModifiers");
                }
                if (tag != null) {
                    var size = Reflect.fastInvoke(tag, ListTagAccessor.getMethodSize1());
                    if (size instanceof Integer) {
                        var list = new ArrayList<ItemAttribute>((int) size);
                        for (var i = 0; i < (int) size; i++) {
                            var t = (CompoundTag) NBTVanillaSerializer.deserialize(Reflect.fastInvoke(tag, ListTagAccessor.getMethodGet1(), i));
                            list.add(new ItemAttribute(
                                    AttributeType.of(StringUtils.camelToSnake(((StringTag) t.tag("AttributeName")).value())),
                                    new UUID(((NumericTag) t.tag("UUIDMost")).longValue(), ((NumericTag) t.tag("UUIDLeast")).longValue()),
                                    ((StringTag) t.tag("Name")).value(),
                                    ((NumericTag) t.tag("Amount")).doubleValue(),
                                    AttributeModifier.Operation.byOrdinal(((NumericTag) t.tag("Operation")).intValue()),
                                    t.hasTag("Slot") ? EquipmentSlot.of(((StringTag) t.tag("Slot")).value()) : null
                            ));
                        }
                        return list;
                    }
                }
            }
        }
        return List.of();
    }

    @Override
    public @NotNull List<@NotNull Enchantment> getEnchantments() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            var list = new ArrayList<Enchantment>();
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).getStoredEnchants().entrySet().forEach(entry -> {
                    var enchantment = Enchantment.ofNullable(entry);
                    if (enchantment != null) {
                        list.add(enchantment);
                    }
                });
            } else {
                meta.getEnchants().entrySet().forEach(entry ->{
                    var enchantment = Enchantment.ofNullable(entry);
                    if (enchantment != null) {
                        list.add(enchantment);
                    }
                });
            }
            return list;
        }
        return List.of();
    }

    @Override
    public @NotNull ItemData getData() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_META_PDC.isSupported()) { // 1.14+
                return new BukkitItemDataPersistentContainer(meta.getPersistentDataContainer());
            } else if (BukkitFeature.ITEM_META_CUSTOM_TAG.isSupported()) { // 1.13.2
                return new BukkitItemDataCustomTags(meta.getCustomTagContainer());
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
        } else {
            // TODO: create blank instances of PDC for 1.14+
            return new CraftBukkitItemData(new HashMap<>());
        }
    }

    @Override
    public @NotNull List<@NotNull HideFlags> getHideFlags() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return meta.getItemFlags().stream().map(ItemFlag::name).map(HideFlags::convert).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public @Nullable Integer getCustomModelData() {
        var meta = wrappedObject.getItemMeta();
        if (BukkitFeature.ITEM_META_CUSTOM_MODEL_DATA.isSupported()) {
            return meta.getCustomModelData();
        }
        return null;
    }

    @Override
    public boolean isUnbreakable() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_META_IS_UNBREAKABLE.isSupported()) {
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
    public @NotNull ItemStackBuilder builder() {
        return new BukkitItemBuilder(wrappedObject.clone());
    }

    @Override
    public boolean isSimilar(@NotNull ItemStack item) {
        return wrappedObject.isSimilar(item.as(org.bukkit.inventory.ItemStack.class));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull ItemStack clone() {
        return new BukkitItem(wrappedObject.clone());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return ItemStackFactory.convertItem(this, type);
        }
    }

    @Override
    public @NotNull CompoundTag getTag() {
        var isMutable = this instanceof ItemStackView; // ItemStackView is a mutable item, don't cache if the item can randomly change
        if (!isMutable && tagCache != null) {
            return tagCache;
        }

        final var nmsStack = ClassStorage.stackAsNMS(wrappedObject);
        final var nbtTag = Reflect.fastInvoke(nmsStack, ItemStackAccessor.getMethodGetTag1());

        if (nbtTag == null) {
            return CompoundTag.EMPTY;
        }

        var tag = NBTVanillaSerializer.deserialize(nbtTag);
        if (tag instanceof CompoundTag) {
            if (!isMutable) {
                tagCache = (CompoundTag) tag;
            }
            return (CompoundTag) tag;
        }

        return CompoundTag.EMPTY;
    }

    @Override
    public @NotNull CompoundTag asCompoundTag() {
        if (isAir()) {
            return CompoundTag.EMPTY;
        }

        final var nmsStack = Reflect.fastInvoke(ClassStorage.stackAsNMS(wrappedObject), ItemStackAccessor.getMethodCopy1());
        final var compound = Reflect.fastInvoke(nmsStack, ItemStackAccessor.getMethodSave1(), Reflect.construct(CompoundTagAccessor.getConstructor0()));

        var tag = NBTVanillaSerializer.deserialize(compound);
        if (tag instanceof CompoundTag) {
            return (CompoundTag) tag;
        }

        return CompoundTag.EMPTY;
    }

    @Override
    public @NotNull String toString() {
        return wrappedObject.toString();
    }
}
