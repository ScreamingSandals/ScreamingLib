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

package org.screamingsandals.lib.minestom.item.builder;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.minestom.item.MinestomItem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// TODO: complete this
@NoArgsConstructor
public class MinestomItemBuilder implements ItemBuilder {
    private Material type;
    private int durability;
    private int amount;
    private Component displayName;
    private List<Component> lore;

    @Override
    public ItemBuilder type(@NotNull ItemTypeHolder type) {
        this.type = type.as(Material.class);
        return this;
    }

    @Override
    public ItemBuilder durability(int durability) {
        this.durability = durability;
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public ItemBuilder displayName(@Nullable Component displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public ItemBuilder itemLore(@Nullable List<@NotNull Component> lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public ItemBuilder attributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers) {
        return this;
    }

    @Override
    public ItemBuilder attributeModifier(@NotNull ItemAttributeHolder modifier) {
        return this;
    }

    @Override
    public ItemBuilder data(@NotNull ItemData data) {
        return this;
    }

    @Override
    public ItemBuilder hideFlags(@Nullable List<@NotNull HideFlags> flags) {
        return this;
    }

    @Override
    public ItemBuilder hideFlag(@NotNull HideFlags flag) {
        return this;
    }

    @Override
    public ItemBuilder enchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments) {
        return this;
    }

    @Override
    public ItemBuilder enchantment(@NotNull EnchantmentHolder enchantment) {
        return this;
    }

    @Override
    public ItemBuilder customModelData(@Nullable Integer data) {
        return this;
    }

    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
        return this;
    }

    @Override
    public ItemBuilder repairCost(int repairCost) {
        return this;
    }

    @Override
    public Optional<Item> build() {
        return Optional.of(
                new MinestomItem(
                        ItemStack.builder(type)
                                .amount(amount)
                                .displayName(displayName)
                                .lore(lore)
                                .build()
                )
        );
    }

    @Override
    public ItemBuilder platformMeta(Object meta) {
        return this;
    }

    @Override
    public ItemBuilder lore(@NotNull Component component) {
        return this;
    }

    @Override
    public boolean supportsMetadata(MetadataKey<?> key) {
        return false;
    }

    @Override
    public boolean supportsMetadata(MetadataCollectionKey<?> key) {
        return false;
    }

    @Override
    public <T> ItemBuilder setMetadata(MetadataKey<T> key, T value) {
        return this;
    }

    @Override
    public <T> ItemBuilder setMetadata(MetadataCollectionKey<T> key, Collection<T> value) {
        return this;
    }

    @Override
    public <T> ItemBuilder addToListMetadata(MetadataCollectionKey<T> key, T value) {
        return this;
    }
}
