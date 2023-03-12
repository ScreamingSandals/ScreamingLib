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
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemMeta;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataConsumer;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.key.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public interface ItemStackBuilder extends MetadataConsumer {
    @Contract("_ -> this")
    @NotNull ItemStackBuilder type(@NotNull ItemTypeHolder type);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder durability(int durability);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder amount(int amount);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder displayName(@Nullable Component displayName);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder itemLore(@Nullable List<@NotNull Component> lore);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder attributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder attributeModifier(@NotNull ItemAttributeHolder modifier);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder data(@NotNull ItemData data);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder hideFlag(@NotNull HideFlags flag);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder enchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder enchantment(@NotNull EnchantmentHolder enchantment);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder customModelData(@Nullable Integer data);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder unbreakable(boolean unbreakable);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder repairCost(int repairCost);

    @Contract("_ -> this")
    @NotNull ItemStackBuilder tag(@NotNull CompoundTag tag);

    @Contract(pure = true)
    @Nullable ItemStack build();

    @Deprecated
    @Contract("_ -> this")
    @NotNull ItemStackBuilder platformMeta(Object meta);

    // DSL

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Contract("_ -> this")
    default @NotNull ItemStackBuilder type(@NotNull Object type) {
        if (type instanceof ItemTypeHolder) {
            return type((ItemTypeHolder) type);
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

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Contract("_,_ -> this")
    default @NotNull ItemStackBuilder enchant(@NotNull Object enchant, int level) {
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

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Contract("_ -> this")
    default @NotNull ItemStackBuilder enchant(@NotNull Object enchant) {
        var enchantment = EnchantmentHolder.ofNullable(enchant);
        if (enchantment != null) {
            this.enchantment(enchantment);
        }
        return this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Contract("_ -> this")
    default @NotNull ItemStackBuilder potion(@NotNull Object potion) {
        NullableExtension.ifNotNull(PotionHolder.ofNullable(potion), potionHolder -> this.setMetadata(ItemMeta.POTION_TYPE, potionHolder));
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder attribute(@NotNull Object itemAttribute) {
        var attribute = AttributeMapping.wrapItemAttribute(itemAttribute);
        if (attribute != null) {
            this.attributeModifier(attribute);
        }
        return this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Contract("_ -> this")
    default @NotNull ItemStackBuilder effect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> NullableExtension.ifNotNull(PotionEffectHolder.ofNullable(effect1), potionEffectHolder -> this.addToListMetadata(ItemMeta.CUSTOM_POTION_EFFECTS, potionEffectHolder)));
            return this;
        }

        NullableExtension.ifNotNull(PotionEffectHolder.ofNullable(effect), potionEffectHolder -> this.addToListMetadata(ItemMeta.CUSTOM_POTION_EFFECTS, potionEffectHolder));
        return this;
    }


    @Contract("_ -> this")
    default @NotNull ItemStackBuilder recipe(@NotNull String key) {
        return recipe(ResourceLocation.of(key));
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder recipe(@NotNull ResourceLocation key) {
        this.addToListMetadata(ItemMeta.RECIPES, key);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder color(@NotNull String color) {
        var c = Color.hexOrName(color);
        if (c != null) {
            return color(c);
        }
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder color(@NotNull Color color) {
        if (this.supportsMetadata(ItemMeta.CUSTOM_POTION_COLOR)) {
            this.setMetadata(ItemMeta.CUSTOM_POTION_COLOR, color);
        } else {
            this.setMetadata(ItemMeta.COLOR, color);
        }
        return this;
    }

    @Contract("_,_,_ -> this")
    default @NotNull ItemStackBuilder color(int r, int g, int b) {
        return color(Color.rgb(r, g, b));
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder skullOwner(@Nullable String skullOwner) {
        this.setMetadata(ItemMeta.SKULL_OWNER, skullOwner);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder fireworkEffect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> NullableExtension.ifNotNull(FireworkEffectHolder.ofNullable(effect1), fireworkEffectHolder -> this.addToListMetadata(ItemMeta.FIREWORK_EFFECTS, fireworkEffectHolder)));
            return this;
        }

        NullableExtension.ifNotNull(FireworkEffectHolder.ofNullable(effect), fireworkEffectHolder -> {
            if (this.supportsMetadata(ItemMeta.FIREWORK_EFFECTS)) {
                this.addToListMetadata(ItemMeta.FIREWORK_EFFECTS, fireworkEffectHolder);
            } else {
                this.setMetadata(ItemMeta.FIREWORK_STAR_EFFECT, fireworkEffectHolder);
            }
        });
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder power(int power) {
        this.setMetadata(ItemMeta.FIREWORK_POWER, power);
        return this;
    }

    @Contract("_ -> this")
    default @NotNull ItemStackBuilder damage(int damage) {
        return durability(damage);
    }

    @Override
    @Contract("_,_ -> this")
    @Deprecated
    <T> @NotNull ItemStackBuilder setMetadata(MetadataKey<T> key, T value);

    @Override
    @Contract("_,_ -> this")
    @Deprecated
    <T> @NotNull ItemStackBuilder setMetadata(MetadataCollectionKey<T> key, Collection<T> value);

    @Override
    @Contract("_,_ -> this")
    @Deprecated
    <T> @NotNull ItemStackBuilder addToListMetadata(MetadataCollectionKey<T> key, T value);
}
