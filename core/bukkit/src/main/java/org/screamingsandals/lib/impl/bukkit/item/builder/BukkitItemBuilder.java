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

package org.screamingsandals.lib.impl.bukkit.item.builder;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemType1_8;
import org.screamingsandals.lib.impl.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.impl.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.impl.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.impl.bukkit.nbt.NBTVanillaSerializer;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemTagKeys;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.impl.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ItemStackAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ListTagAccessor;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.StringUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BukkitItemBuilder implements ItemStackBuilder {
    private org.bukkit.inventory.ItemStack item;

    @Override
    public @NotNull ItemStackBuilder type(@NotNull ItemType type) {
        if (item == null) {
            item = new org.bukkit.inventory.ItemStack(Material.AIR);
        }

        this.item.setType(type.as(Material.class));
        if (type instanceof BukkitItemType1_8) {
            var durability = ((BukkitItemType1_8) type).forcedDurability();
            if (durability != 0) {
                durability(durability);
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder durability(int durability) {
        return durability((short) durability);
    }

    public ItemStackBuilder durability(short durability) {
        if (!BukkitFeature.FLATTENING.isSupported()) {
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
    public @NotNull ItemStackBuilder attributeModifiers(@Nullable List<@NotNull ItemAttribute> modifiers) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_ATTRIBUTE_MODIFIERS_API.isSupported()) { // 1.13.1
                meta.setAttributeModifiers(null);
                if (modifiers != null) {
                    modifiers.stream()
                            .map(holder -> holder.as(BukkitItemAttribute.class))
                            .forEach(holder -> meta.addAttributeModifier(holder.getAttribute(), holder.getAttributeModifier()));
                }
                item.setItemMeta(meta);
            } else {
                // Pre 1.13.1
                if (modifiers != null) {
                    fillStackWithModifiers(
                            modifiers.stream()
                                    .map(this::constructPre1_13AttributeModifier)
                                    .map(NBTVanillaSerializer::serialize)
                                    .collect(Collectors.toList())
                    );
                } else {
                    if (!ClassStorage.CB.CraftItemStack.isInstance(item)) {
                        item = ClassStorage.asCBStack(item);
                    }

                    var nbt = Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_GET_TAG.get());

                    if (nbt != null && Reflect.fastInvoke(nbt, CompoundTagAccessor.METHOD_GET.get(), "AttributeModifiers") != null) {
                        Reflect.fastInvoke(nbt, CompoundTagAccessor.METHOD_REMOVE.get(), "AttributeModifiers");

                        Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_SET_TAG.get(), nbt);
                    }
                }
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder attributeModifier(@NotNull ItemAttribute modifier) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_ATTRIBUTE_MODIFIERS_API.isSupported()) { // 1.13.1
                var mod = modifier.as(BukkitItemAttribute.class);
                meta.addAttributeModifier(mod.getAttribute(), mod.getAttributeModifier());
                item.setItemMeta(meta);
            } else {
                // Pre 1.13.1
                var tag = constructPre1_13AttributeModifier(modifier);

                var serialized = NBTVanillaSerializer.serialize(tag);

                fillStackWithModifiers(List.of(serialized));
            }
        }
        return this;
    }

    private void fillStackWithModifiers(@NotNull List<@NotNull Object> modifiers) {
        if (!ClassStorage.CB.CraftItemStack.isInstance(item)) {
            item = ClassStorage.asCBStack(item);
        }

        var nbt = Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_GET_TAG.get());

        Object attributes = nbt != null ? Reflect.fastInvoke(nbt, CompoundTagAccessor.METHOD_GET.get(), "AttributeModifiers") : null;
        if (nbt == null) {
            nbt = Reflect.construct(CompoundTagAccessor.CONSTRUCTOR_0.get());
        }
        if (attributes == null || !ListTagAccessor.TYPE.get().isInstance(attributes)) {
            attributes = Reflect.construct(ListTagAccessor.CONSTRUCTOR_0.get());
            Reflect.fastInvoke(nbt, CompoundTagAccessor.METHOD_PUT.get(), "AttributeModifiers", attributes);
        }
        for (var modifier : modifiers) {
            Reflect.fastInvoke(attributes, ListTagAccessor.METHOD_ADD_1.get(), modifier);
        }

        Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_SET_TAG.get(), nbt);
    }

    private @NotNull CompoundTag constructPre1_13AttributeModifier(@NotNull ItemAttribute modifier) {
        CompoundTag ct = CompoundTag.EMPTY
                .with("AttributeName", StringUtils.snakeToCamel(modifier.getType().location().path()))
                .with("Name", modifier.getName())
                .with("Amount", modifier.getAmount())
                .with("Operation", modifier.getOperation().ordinal())
                .with("UUIDLeast", modifier.getUuid().getLeastSignificantBits())
                .with("UUIDMost", modifier.getUuid().getMostSignificantBits());
        if (modifier.getSlot() != null) {
            String value = null;
            switch (modifier.getSlot().platformName()) {
                case "HAND":
                    if (BukkitFeature.OFF_HAND.isSupported()) {
                        value = "mainhand";
                    } else {
                        value = "hand";
                    }
                    break;
                case "OFF_HAND": value = "offhand"; break;
                case "FEET": value = "feet"; break;
                case "LEGS": value = "legs"; break;
                case "CHEST": value = "chest"; break;
                case "HEAD": value = "head"; break;
            }
            if (value != null) {
                ct = ct.with("Slot", value);
            }
        }
        return ct;
    }

    @Override
    public @NotNull ItemStackBuilder data(@NotNull Consumer<@NotNull ItemData> dataBuilder) {
        if (item == null) {
            return this;
        }

        var meta = item.getItemMeta();
        if (meta != null) {
            if (BukkitFeature.ITEM_META_PDC.isSupported()) { // 1.14+
                dataBuilder.accept(new BukkitItemDataPersistentContainer(meta.getPersistentDataContainer()));
                item.setItemMeta(meta);
            } else if (BukkitFeature.ITEM_META_CUSTOM_TAG.isSupported()) { // 1.13.2
                dataBuilder.accept(new BukkitItemDataCustomTags(meta.getCustomTagContainer()));
                item.setItemMeta(meta);
            } else {
                var unhandled = (Map<String,Object>) Reflect.getField(meta, "unhandledTags");
                Object compound;
                if (unhandled.containsKey("PublicBukkitValues")) {
                    compound = unhandled.get("PublicBukkitValues");
                } else {
                    compound = Reflect.construct(CompoundTagAccessor.CONSTRUCTOR_0.get());
                    unhandled.put("PublicBukkitValues", compound);
                }
                var nmap = new HashMap<String, Object>();
                if (CompoundTagAccessor.TYPE.get().isInstance(compound)) {
                    var keys = (Set) Reflect.fastInvoke(compound, CompoundTagAccessor.METHOD_GET_ALL_KEYS.get());
                    for (var key : keys) {
                        nmap.put(key.toString(), Reflect.fastInvoke(compound, CompoundTagAccessor.METHOD_GET.get(), key));
                    }
                }
                var cbItemData = new CraftBukkitItemData(nmap);
                dataBuilder.accept(cbItemData);
                cbItemData.getKeyNBTMap().forEach((s, o) ->
                        Reflect.fastInvoke(compound, CompoundTagAccessor.METHOD_PUT.get(), s, o)
                );
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
            if (BukkitFeature.ITEM_META_PDC.isSupported()) { // 1.14+
                if (data instanceof BukkitItemDataPersistentContainer && !data.isEmpty()) {
                    var origDataContainer = ((BukkitItemDataPersistentContainer) data).getDataContainer();
                    Reflect.getMethod(meta.getPersistentDataContainer(), "putAll", Map.class)
                            .invoke(Reflect.fastInvoke(origDataContainer, "getRaw"));
                    item.setItemMeta(meta);
                }
            } else if (BukkitFeature.ITEM_META_CUSTOM_TAG.isSupported()) { // 1.13.2
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
                        compound = Reflect.construct(CompoundTagAccessor.CONSTRUCTOR_0.get());
                        unhandled.put("PublicBukkitValues", compound);
                    }
                    ((CraftBukkitItemData) data).getKeyNBTMap().forEach((s, o) ->
                            Reflect.fastInvoke(compound, CompoundTagAccessor.METHOD_PUT.get(), s, o)
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
            } catch (IllegalArgumentException ignored) {
                // unknown flag
            }
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
            } catch (IllegalArgumentException ignored) {
                // unknown flag
            }
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
            if (BukkitFeature.ITEM_META_CUSTOM_MODEL_DATA.isSupported()) {
                meta.setCustomModelData(data);
                item.setItemMeta(meta);
            }
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
            if (BukkitFeature.ITEM_META_IS_UNBREAKABLE.isSupported()) {
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
        Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_SET_TAG.get(), NBTVanillaSerializer.serialize(tag));
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder mergeTag(@NotNull CompoundTag tag) {
        if (item == null) {
            item = new org.bukkit.inventory.ItemStack(Material.AIR); // shouldn't we throw error instead?
        }

        if (!ClassStorage.CB.CraftItemStack.isInstance(item)) {
            item = ClassStorage.asCBStack(item);
        }

        var serialized = NBTVanillaSerializer.serialize(tag);

        var nbt = Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_GET_TAG.get());

        if (nbt != null) {
            Reflect.fastInvoke(nbt, CompoundTagAccessor.METHOD_MERGE.get(), serialized);
        } else {
            nbt = serialized;
        }

        Reflect.fastInvoke(ClassStorage.getHandleOfItemStack(item), ItemStackAccessor.METHOD_SET_TAG.get(), nbt);
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
    public @NotNull ItemStackBuilder potion(@NotNull Object potion) {
        var potionData = Potion.ofNullable(potion);
        if (item != null && potionData != null) {
            var meta = item.getItemMeta();
            if (meta instanceof PotionMeta) {
                if (BukkitFeature.POTION_API.isSupported()) {
                    ((PotionMeta) meta).setBasePotionData(potionData.as(PotionData.class));
                    item.setItemMeta(meta);
                } else {
                    item.setDurability(potionData.as(org.bukkit.potion.Potion.class).toDamageValue());
                }
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder effect(@NotNull Object effect) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof PotionMeta) {
                if (effect instanceof List) {
                    final var list = (List<?>) effect;
                    list.forEach(effect1 -> {
                        var potionEffect = PotionEffect.ofNullable(effect1);
                        if (potionEffect != null) {
                            ((PotionMeta) meta).addCustomEffect(potionEffect.as(org.bukkit.potion.PotionEffect.class), true);
                        }
                    });
                    item.setItemMeta(meta);
                    return this;
                }

                var potionEffect = PotionEffect.ofNullable(effect);
                if (potionEffect != null) {
                    ((PotionMeta) meta).addCustomEffect(potionEffect.as(org.bukkit.potion.PotionEffect.class), true);
                    item.setItemMeta(meta);
                }
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder recipe(@NotNull ResourceLocation key) {
        if (BukkitFeature.KNOWLEDGE_BOOK_META.isSupported() && item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof KnowledgeBookMeta) {
                ((KnowledgeBookMeta) meta).addRecipe(new NamespacedKey(key.namespace(), key.path()));
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder color(@NotNull Color color)  {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(org.bukkit.Color.fromRGB(color.red(), color.green(), color.blue()));
                item.setItemMeta(meta);
                return this;
            }
        }

        if (BukkitFeature.POTION_META_COLOR.isSupported()) {
            if (item != null) {
                var meta = item.getItemMeta();
                if (meta instanceof PotionMeta) {
                    ((PotionMeta) meta).setColor(org.bukkit.Color.fromRGB(color.red(), color.green(), color.blue()));
                    item.setItemMeta(meta);
                }
            }
            return this;
        } else {
            return mergeTag(CompoundTag.EMPTY.with(ItemTagKeys.CUSTOM_POTION_COLOR, color.compoundRgb())); // are we supposed to use NBT? was it even supported by Vanilla MC back then?
        }
    }

    @Override
    public @NotNull ItemStackBuilder skullOwner(@Nullable String skullOwner)  {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                ((SkullMeta) meta).setOwner(skullOwner);
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder fireworkEffect(@NotNull Object effect) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof FireworkMeta) {
                if (effect instanceof List) {
                    final var list = (List<?>) effect;
                    list.forEach(effect1 -> {
                        var fireworkEffect = FireworkEffect.ofNullable(effect1);
                        if (fireworkEffect != null) {
                            ((FireworkMeta) meta).addEffect(fireworkEffect.as(org.bukkit.FireworkEffect.class));
                        }
                    });
                    item.setItemMeta(meta);
                    return this;
                }

                var fireworkEffect = FireworkEffect.ofNullable(effect);
                if (fireworkEffect != null) {
                    ((FireworkMeta) meta).addEffect(fireworkEffect.as(org.bukkit.FireworkEffect.class));
                    item.setItemMeta(meta);
                }
            } else if (meta instanceof FireworkEffectMeta) {
                var fireworkEffect = FireworkEffect.ofNullable(effect);
                if (fireworkEffect != null) {
                    ((FireworkEffectMeta) meta).setEffect(fireworkEffect.as(org.bukkit.FireworkEffect.class));
                    item.setItemMeta(meta);
                }
            }
        }
        return this;
    }

    @Override
    public @NotNull ItemStackBuilder power(int power) {
        if (item != null) {
            var meta = item.getItemMeta();
            if (meta instanceof FireworkMeta) {
                ((FireworkMeta) meta).setPower(power);
                item.setItemMeta(meta);
            }
        }
        return this;
    }
}
