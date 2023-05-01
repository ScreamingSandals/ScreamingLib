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

package org.screamingsandals.lib.item.builder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.item.builder.ShortStackDeserializer;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
public interface ItemStackBuilder {
    @Contract("_ -> this")
    @NotNull ItemStackBuilder type(@NotNull ItemType type);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder durability(int durability);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder amount(int amount);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder displayName(@Nullable Component displayName);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder itemLore(@Nullable List<@NotNull Component> lore);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder attributeModifiers(@Nullable List<@NotNull ItemAttribute> modifiers);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder attributeModifier(@NotNull ItemAttribute modifier);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder data(@NotNull ItemData data);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder data(@NotNull Consumer<@NotNull ItemData> dataBuilder);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder hideFlag(@NotNull HideFlags flag);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder enchantments(@Nullable List<@NotNull Enchantment> enchantments);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder enchantment(@NotNull Enchantment enchantment);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder customModelData(@Nullable Integer data);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder unbreakable(boolean unbreakable);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder repairCost(int repairCost);

    /**
     * Sets new item tag for this item. This means that everything except amount and type will be overriden.
     *
     * @param tag which will be assigned to this item
     * @return this builder
     * @see #mergeTag(CompoundTag)
     */
    @Contract("_ -> this")
    @NotNull ItemStackBuilder tag(@NotNull CompoundTag tag);

    /**
     * Merges provided tag with this item tag
     *
     * @param tag which will be merged to this item tag
     * @return this builder
     */
    @Contract("_ -> this")
    @NotNull ItemStackBuilder mergeTag(@NotNull CompoundTag tag);

    @Contract(pure = true)
    @Nullable ItemStack build();

    @Deprecated
    @Contract("_ -> this")
    @NotNull ItemStackBuilder platformMeta(Object meta);

    // DSL

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder type(@MinecraftType(MinecraftType.Type.ITEM_TYPE) @NotNull Object type) {
        if (type instanceof ItemType) {
            return type((ItemType) type);
        }
        ShortStackDeserializer.deserializeShortStack(this, type);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder name(@NotNull Component name) {
        return displayName(name);
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder name(@NotNull ComponentLike name) {
        return displayName(name.asComponent());
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder name(@Nullable String name) {
        return displayName(name == null ? null : Component.fromLegacy(name));
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder localizedName(@Nullable String name) {
        return displayName(name == null ? null : Component.translatable().translate(name).build());
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder repair(int repair) {
        return repairCost(repair);
    }

    @Contract("_ -> this")
    default <C> @NotNull ItemStackBuilder flags(@Nullable List<@NotNull C> flags) {
        if (flags == null) {
            return this;
        } else {
            return hideFlags(
                    flags.stream()
                            .map(o -> {
                                if (o instanceof HideFlags) {
                                    return (HideFlags) o;
                                } else {
                                    return HideFlags.convert(o.toString());
                                }
                            }).collect(Collectors.toList())
            );
        }
    }

    /**
     * Adds new line to the lore.
     *
     * @param component
     * @return
     */
    @Contract("_ -> this")
    @NotNull ItemStackBuilder lore(@NotNull Component component);

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder lore(@NotNull ComponentLike component) {
        return lore(component.asComponent());
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder lore(@Nullable String lore) {
        return lore(lore == null ? Component.empty() : Component.fromLegacy(lore));
    }

    @Contract("_ -> this")
    default <C> @NotNull ItemStackBuilder lore(@NotNull List<@Nullable C> lore) {
        return itemLore(lore.stream()
                .map(c -> {
                    if (c instanceof ComponentLike) {
                        return ((ComponentLike) c).asComponent();
                    } else {
                        return c == null ? Component.empty() : Component.fromLegacy(c.toString());
                    }
                })
                .collect(Collectors.toList())
        );
    }

    @Contract("_,_ -> this")
    default @NotNull ItemStackBuilder enchant(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @NotNull Object enchant, int level) {
        return enchant(enchant + " " + level);
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder enchant(@NotNull Map<@NotNull Object, Integer> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder enchant(@NotNull List<@NotNull Object> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder enchant(@MinecraftType(MinecraftType.Type.ENCHANTMENT) @NotNull Object enchant) {
        var enchantment = Enchantment.ofNullable(enchant);
        if (enchantment != null) {
            this.enchantment(enchantment);
        }
        return this;
    }

    @Contract("_ -> this")
    @NotNull ItemStackBuilder potion(@MinecraftType(MinecraftType.Type.POTION) @NotNull Object potion);

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder attribute(@NotNull Object itemAttribute) {
        var attribute = Attributes.wrapItemAttribute(itemAttribute);
        if (attribute != null) {
            this.attributeModifier(attribute);
        }
        return this;
    }

    @Contract("_ -> this")
    @NotNull ItemStackBuilder effect(@MinecraftType(MinecraftType.Type.POTION_EFFECT) @NotNull Object effect);

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder recipe(@NotNull String key) {
        return recipe(ResourceLocation.of(key));
    }

    @Contract("_ -> this")
    @NotNull ItemStackBuilder recipe(@NotNull ResourceLocation key);

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder color(@NotNull String color) {
        return color(Color.hexOrName(color));
    }

    @Contract("_ -> this")
    @NotNull ItemStackBuilder color(@NotNull Color color);

    @Contract("_,_,_ -> this")
    default @NotNull ItemStackBuilder color(int r, int g, int b) {
        return color(Color.rgb(r, g, b));
    }

    @Contract("_ -> this")
    @NotNull ItemStackBuilder skullOwner(@Nullable String skullOwner);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder fireworkEffect(@NotNull Object effect);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder power(int power);

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder damage(int damage) {
        return durability(damage);
    }
}
