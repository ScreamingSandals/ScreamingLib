package org.screamingsandals.lib.item.builder;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;

import java.util.List;
import java.util.Optional;

public interface ItemBuilder {
    ItemBuilder type(@NotNull ItemTypeHolder type);

    ItemBuilder amount(int amount);

    ItemBuilder displayName(@Nullable Component displayName);

    ItemBuilder lore(@Nullable List<@NotNull Component> lore);

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

}
