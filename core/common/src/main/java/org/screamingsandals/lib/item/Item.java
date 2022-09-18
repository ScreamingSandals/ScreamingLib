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

package org.screamingsandals.lib.item;

import com.iamceph.resulter.core.pack.ProtoWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.metadata.MetadataProvider;
import org.screamingsandals.lib.nbt.*;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContentLike;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an immutable Item.
 */
public interface Item extends ComparableWrapper, RawValueHolder, ParticleData, Cloneable, CompoundTagHolder, CompoundTagLike, CompoundTagTreeInspector, ItemContentLike, MetadataProvider, ProtoWrapper<ProtoItem> {
    @NotNull ItemTypeHolder getType();

    default @NotNull ItemTypeHolder getMaterial() { // alternative getter (old name)
        return getType();
    }

    int getAmount();

    @Nullable Component getDisplayName();

    @NotNull List<@NotNull Component> getLore();

    @NotNull List<@NotNull ItemAttributeHolder> getAttributeModifiers();

    default @NotNull List<@NotNull ItemAttributeHolder> getItemAttributes() { // alternative getter (old name)
        return getAttributeModifiers();
    }

    @NotNull List<@NotNull EnchantmentHolder> getEnchantments();

    ItemData getData();

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

    @NotNull ItemBuilder builder();

    default boolean isAir() {
        return getType().isAir();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    default boolean is(@NotNull Object @NotNull... objects) {
        return getType().is(objects);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    default boolean is(@NotNull Object object) {
        return getType().is(object);
    }

    boolean isSimilar(@NotNull Item item);

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withType(@NotNull ItemTypeHolder type) {
        return builder().type(type).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withAmount(int amount) {
        return builder().amount(amount).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withDisplayName(@Nullable Component displayName) {
        return builder().displayName(displayName).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withItemLore(@Nullable List<@NotNull Component> lore) {
        return builder().itemLore(lore).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withAttributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers) {
        return builder().attributeModifiers(modifiers).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withAttributeModifier(@NotNull ItemAttributeHolder modifier) {
        return builder().attributeModifier(modifier).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withData(@NotNull ItemData data) {
        return builder().data(data).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withHideFlags(@Nullable List<@NotNull HideFlags> flags) {
        return builder().hideFlags(flags).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withHideFlag(@NotNull HideFlags flag) {
        return builder().hideFlag(flag).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withEnchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments) {
        return builder().enchantments(enchantments).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withEnchantment(@NotNull EnchantmentHolder enchantment) {
        return builder().enchantment(enchantment).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withCustomModelData(@Nullable Integer customModelData) {
        return builder().customModelData(customModelData).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withUnbreakable(boolean unbreakable) {
        return builder().unbreakable(unbreakable).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withRepairCost(int repairCost) {
        return builder().repairCost(repairCost).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Item withTag(@NotNull CompoundTag tag) {
        return builder().tag(tag).build().orElseThrow();
    }

    @Override
    default @Nullable Tag findTag(@NotNull String @NotNull ... tagKeys) {
        return getTag().findTag(tagKeys);
    }

    @NotNull Item clone();

    @Override
    default @NotNull ItemContent asItemContent() {
        var tag = getTag();
        return ItemContent.builder()
                .id(NamespacedMappingKey.of(getMaterial().platformName())) // TODO: implement namespaced holders and get the actual namespaced key
                .count(getAmount())
                .tag(tag.isEmpty() ? null : tag)
                .build();
    }

    @Override
    default ProtoItem asProto() {
        final var builder = ProtoItem.newBuilder();
        builder.setType(getType().asProto())
                .setAmount(getAmount());

        final var displayName = getDisplayName();
        if (displayName != null) {
            builder.setDisplayName(displayName.toJavaJson());
        }

        final var lore = getLore();
        if (lore != null) {
            builder.addAllLore(lore.stream()
                    .map(Component::toJavaJson)
                    .collect(Collectors.toList()));
        }

        builder.addAllAttributeModifiers(getAttributeModifiers()
                        .stream()
                        .map(ProtoWrapper::asProto)
                        .collect(Collectors.toList()))
                .addAllEnchantments(getEnchantments().stream()
                        .map(ProtoWrapper::asProto)
                        .collect(Collectors.toList()))
                .addAllHideFlags(getHideFlags().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .setCustomModelData(getCustomModelData() == null ? 0 : getCustomModelData())
                .setUnbreakable(isUnbreakable())
                .setRepairCost(getRepairCost());

        return builder.build();
    }
}
