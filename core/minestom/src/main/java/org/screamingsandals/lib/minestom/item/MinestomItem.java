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

package org.screamingsandals.lib.minestom.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.minestom.item.meta.MinestomEnchantmentHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MinestomItem extends BasicWrapper<ItemStack> implements Item {
    public MinestomItem(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ItemTypeHolder getType() {
        return ItemTypeHolder.of(wrappedObject.getMaterial());
    }

    @Override
    public int getAmount() {
        return wrappedObject.getAmount();
    }

    @Override
    public @Nullable Component getDisplayName() {
        return wrappedObject.getDisplayName();
    }

    @Override
    public List<Component> getLore() {
        return wrappedObject.getLore();
    }

    @Override
    public List<ItemAttributeHolder> getAttributeModifiers() {
        return wrappedObject.getMeta().getAttributes().stream()
                .map(AttributeMapping::wrapItemAttribute)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnchantmentHolder> getEnchantments() {
        return wrappedObject.getMeta().getEnchantmentMap().entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .map(MinestomEnchantmentHolder::new)
                .collect(Collectors.toList());
    }

    @Override
    public ItemData getData() {
        return null;
    }

    @Override
    public List<HideFlags> getHideFlags() {
        return null;
    }

    @Override
    public Integer getCustomModelData() {
        return wrappedObject.getMeta().getCustomModelData();
    }

    @Override
    public boolean isUnbreakable() {
        return wrappedObject.getMeta().isUnbreakable();
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

    @Override
    public ItemBuilder builder() {
        return null;
    }

    @Override
    public boolean isSimilar(Item item) {
        return wrappedObject.isSimilar(item.as(ItemStack.class));
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Item clone() {
        //noinspection UnstableApiUsage
        return new MinestomItem(ItemStack.fromItemNBT(wrappedObject.toItemNBT()));
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
    public <T> @Nullable T getMetadata(MetadataKey<T> key) {
        return null;
    }

    @Override
    public <T> Optional<T> getMetadataOptional(MetadataKey<T> key) {
        return Optional.empty();
    }

    @Override
    public @Nullable <T> Collection<T> getMetadata(MetadataCollectionKey<T> key) {
        return null;
    }
}
