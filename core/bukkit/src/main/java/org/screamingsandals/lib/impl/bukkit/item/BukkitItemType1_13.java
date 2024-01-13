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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlock1_13;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitItemType1_13 extends BasicWrapper<Material> implements ItemType {
    public BukkitItemType1_13(@NotNull Material wrappedObject) {
        super(wrappedObject);
        if (!wrappedObject.isItem()) {
            throw new UnsupportedOperationException("BukkitItemTypeHolder can wrap only item types!!!");
        }
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.getMaxStackSize();
    }

    @Override
    public @Nullable Block block() {
        if (!wrappedObject.isBlock()) {
            return null;
        }
        return new BukkitBlock1_13(wrappedObject);
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        // native tags
        var bukkitTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, new NamespacedKey(key.namespace(), key.path()), Material.class);
        if (bukkitTag != null) {
            return bukkitTag.isTagged(wrappedObject);
        }
        // backported tags
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        var value = key.path();
        return BukkitItemTypeRegistry1_13.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Material || object instanceof ItemType) {
            return equals(object);
        }
        if (object instanceof String) {
            var str = (String) object;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(ItemType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == ItemStack.class) {
            return (T) new ItemStack(wrappedObject);
        }
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        var key = wrappedObject.getKey();
        return ResourceLocation.of(key.getNamespace(), key.getKey());
    }
}
