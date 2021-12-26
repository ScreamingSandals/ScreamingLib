package org.screamingsandals.lib.item.builder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemMeta;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataConsumer;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ItemBuilder extends MetadataConsumer {
    ItemBuilder type(@NotNull ItemTypeHolder type);

    ItemBuilder durability(int durability);

    ItemBuilder amount(int amount);

    ItemBuilder displayName(@Nullable Component displayName);

    ItemBuilder itemLore(@Nullable List<@NotNull Component> lore);

    ItemBuilder attributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers);

    ItemBuilder attributeModifier(@NotNull ItemAttributeHolder modifier);

    ItemBuilder data(@NotNull ItemData data);

    ItemBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags);

    ItemBuilder hideFlag(@NotNull HideFlags flag);

    ItemBuilder enchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments);

    ItemBuilder enchantment(@NotNull EnchantmentHolder enchantment);

    ItemBuilder customModelData(@Nullable Integer data);

    ItemBuilder unbreakable(boolean unbreakable);

    ItemBuilder repairCost(int repairCost);

    Optional<Item> build();

    @Deprecated
    ItemBuilder platformMeta(Object meta);

    // DSL

    default ItemBuilder type(@NotNull Object type) {
        if (type instanceof ItemTypeHolder) {
            return type((ItemTypeHolder) type);
        }
        ShortStackDeserializer.deserializeShortStack(this, type);
        return this;
    }

    default ItemBuilder name(@NotNull Component name) {
        return displayName(name);
    }

    default ItemBuilder name(@NotNull ComponentLike name) {
        return displayName(name.asComponent());
    }

    default ItemBuilder name(@Nullable String name) {
        return displayName(name == null ? null : AdventureHelper.toComponent(name));
    }

    default ItemBuilder localizedName(@Nullable String name) {
        return displayName(name == null ? null : Component.translatable(name));
    }

    default ItemBuilder repair(int repair) {
        return repairCost(repair);
    }

    default <C> ItemBuilder flags(@Nullable List<C> flags) {
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
    ItemBuilder lore(@NotNull Component component);

    default ItemBuilder lore(@NotNull ComponentLike component) {
        return lore(component.asComponent());
    }

    default ItemBuilder lore(@Nullable String lore) {
        return lore(lore == null ? Component.empty() : AdventureHelper.toComponent(lore));
    }

    default <C> ItemBuilder lore(@NotNull List<C> lore) {
        return itemLore(lore.stream()
                .map(c -> {
                    if (c instanceof ComponentLike) {
                        return ((ComponentLike) c).asComponent();
                    } else {
                        return c == null ? Component.empty() : AdventureHelper.toComponent(c.toString());
                    }
                })
                .collect(Collectors.toList())
        );
    }

    default ItemBuilder enchant(@NotNull Object enchant, int level) {
        return enchant(enchant + " " + level);
    }

    default ItemBuilder enchant(@NotNull Map<Object, Integer> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    default ItemBuilder enchant(@NotNull List<Object> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    default ItemBuilder enchant(@NotNull Object enchant) {
        EnchantmentHolder.ofOptional(enchant).ifPresent(this::enchantment);
        return this;
    }

    default ItemBuilder potion(@NotNull Object potion) {
        PotionHolder.ofOptional(potion).ifPresent(potionHolder -> {
            this.setMetadata(ItemMeta.POTION_TYPE, potionHolder);
        });
        return this;
    }

    default ItemBuilder attribute(@NotNull Object itemAttribute) {
        AttributeMapping.wrapItemAttribute(itemAttribute).ifPresent(this::attributeModifier);
        return this;
    }

    default ItemBuilder effect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> PotionEffectHolder.ofOptional(effect1).ifPresent(potionEffectHolder -> {
                this.addToListMetadata(ItemMeta.CUSTOM_POTION_EFFECTS, potionEffectHolder);
            }));
            return this;
        }

        PotionEffectHolder.ofOptional(effect).ifPresent(potionEffectHolder -> {
            this.addToListMetadata(ItemMeta.CUSTOM_POTION_EFFECTS, potionEffectHolder);
        });
        return this;
    }


    default ItemBuilder recipe(@NotNull String key) {
        return recipe(NamespacedMappingKey.of(key));
    }

    default ItemBuilder recipe(@NotNull NamespacedMappingKey key) {
        this.addToListMetadata(ItemMeta.RECIPES, key);
        return this;
    }

    default ItemBuilder color(@NotNull String color) {
        var c = TextColor.fromCSSHexString(color);
        if (c != null) {
            return color(c);
        } else {
            var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
            if (c2 != null) {
                return color(c2);
            }
        }
        return this;
    }

    default ItemBuilder color(@NotNull RGBLike color) {
        if (this.supportsMetadata(ItemMeta.CUSTOM_POTION_COLOR)) {
            this.setMetadata(ItemMeta.CUSTOM_POTION_COLOR, color);
        } else {
            this.setMetadata(ItemMeta.COLOR, color);
        }
        return this;
    }

    default ItemBuilder color(int r, int g, int b) {
        return color(TextColor.color(r, g, b));
    }

    default ItemBuilder skullOwner(@Nullable String skullOwner) {
        this.setMetadata(ItemMeta.SKULL_OWNER, skullOwner);
        return this;
    }

    default ItemBuilder fireworkEffect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> FireworkEffectHolder.ofOptional(effect1).ifPresent(fireworkEffectHolder -> {
                this.addToListMetadata(ItemMeta.FIREWORK_EFFECTS, fireworkEffectHolder);
            }));
            return this;
        }

        FireworkEffectHolder.ofOptional(effect).ifPresent(fireworkEffectHolder -> {
            if (this.supportsMetadata(ItemMeta.FIREWORK_EFFECTS)) {
                this.addToListMetadata(ItemMeta.FIREWORK_EFFECTS, fireworkEffectHolder);
            } else {
                this.setMetadata(ItemMeta.FIREWORK_STAR_EFFECT, fireworkEffectHolder);
            }
        });
        return this;
    }

    default ItemBuilder power(int power) {
        this.setMetadata(ItemMeta.FIREWORK_POWER, power);
        return this;
    }

    default ItemBuilder damage(int damage) {
        return durability(damage);
    }

    @Override
    <T> ItemBuilder setMetadata(MetadataKey<T> key, T value);

    @Override
    <T> ItemBuilder setMetadata(MetadataCollectionKey<T> key, Collection<T> value);

    @Override
    <T> ItemBuilder addToListMetadata(MetadataCollectionKey<T> key, T value);
}
