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

package org.screamingsandals.lib.item;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.ItemStackHolder;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.nbt.*;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContentLike;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an immutable Item.
 */
public interface ItemStack extends ComparableWrapper, RawValueHolder, ParticleData, Cloneable, CompoundTagHolder, CompoundTagLike, CompoundTagTreeInspector, ItemContentLike, ItemStackHolder {
    @NotNull ItemType getType();

    default @NotNull ItemType getMaterial() { // alternative getter (old name)
        return getType();
    }

    int getAmount();

    @Nullable Component getDisplayName();

    @NotNull List<@NotNull Component> getLore();

    @NotNull List<@NotNull ItemAttribute> getAttributeModifiers();

    default @NotNull List<@NotNull ItemAttribute> getItemAttributes() { // alternative getter (old name)
        return getAttributeModifiers();
    }

    @NotNull List<@NotNull Enchantment> getEnchantments();

    @NotNull ItemData getData();

    @NotNull List<@NotNull HideFlags> getHideFlags();

    @Deprecated
    default @NotNull List<@NotNull String> getItemFlags() {
        return getHideFlags().stream().map(HideFlags::getBukkitName).collect(Collectors.toList());
    }

    @Nullable Integer getCustomModelData();

    boolean isUnbreakable();

    int getRepairCost();

    default int getRepair() { // alternative getter (old name)
        return getRepairCost();
    }

    @NotNull ItemStackBuilder builder();

    default boolean isAir() {
        return getType().isAir();
    }

    @Override
    default boolean is(@MinecraftType(MinecraftType.Type.ITEM_TYPE) @Nullable Object @NotNull... objects) {
        return getType().is(objects);
    }

    @Override
    default boolean is(@MinecraftType(MinecraftType.Type.ITEM_TYPE) @Nullable Object object) {
        return getType().is(object);
    }

    boolean isSimilar(@NotNull ItemStack item);

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withType(@NotNull ItemType type) {
        return Objects.requireNonNull(builder().type(type).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withAmount(int amount) {
        return Objects.requireNonNull(builder().amount(amount).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withDisplayName(@Nullable Component displayName) {
        return Objects.requireNonNull(builder().displayName(displayName).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withItemLore(@Nullable List<@NotNull Component> lore) {
        return Objects.requireNonNull(builder().itemLore(lore).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withAttributeModifiers(@Nullable List<@NotNull ItemAttribute> modifiers) {
        return Objects.requireNonNull(builder().attributeModifiers(modifiers).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withAttributeModifier(@NotNull ItemAttribute modifier) {
        return Objects.requireNonNull(builder().attributeModifier(modifier).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withData(@NotNull ItemData data) {
        return Objects.requireNonNull(builder().data(data).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withHideFlags(@Nullable List<@NotNull HideFlags> flags) {
        return Objects.requireNonNull(builder().hideFlags(flags).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withHideFlag(@NotNull HideFlags flag) {
        return Objects.requireNonNull(builder().hideFlag(flag).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withEnchantments(@Nullable List<@NotNull Enchantment> enchantments) {
        return Objects.requireNonNull(builder().enchantments(enchantments).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withEnchantment(@NotNull Enchantment enchantment) {
        return Objects.requireNonNull(builder().enchantment(enchantment).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withCustomModelData(@Nullable Integer customModelData) {
        return Objects.requireNonNull(builder().customModelData(customModelData).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withUnbreakable(boolean unbreakable) {
        return Objects.requireNonNull(builder().unbreakable(unbreakable).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withRepairCost(int repairCost) {
        return Objects.requireNonNull(builder().repairCost(repairCost).build());
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull ItemStack withTag(@NotNull CompoundTag tag) {
        return Objects.requireNonNull(builder().tag(tag).build());
    }

    @Override
    default @Nullable Tag findTag(@NotNull String @NotNull... tagKeys) {
        return getTag().findTag(tagKeys);
    }

    @NotNull ItemStack clone();

    @Override
    default @NotNull ItemContent asItemContent() {
        var tag = getTag();
        return ItemContent.builder()
                .id(ResourceLocation.of(getMaterial().platformName())) // TODO: implement namespaced holders and get the actual namespaced key
                .count(getAmount())
                .tag(tag.isEmpty() ? null : tag)
                .build();
    }
}
