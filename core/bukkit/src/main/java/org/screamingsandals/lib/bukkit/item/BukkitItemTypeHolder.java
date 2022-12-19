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

package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.block.BukkitBlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

public class BukkitItemTypeHolder extends BasicWrapper<Material> implements ItemTypeHolder {

    // Because people can be stupid + it's also used in our current code for deserializing items ;)
    private short forcedDurability;

    public BukkitItemTypeHolder(Material wrappedObject) {
        super(wrappedObject);
        if (!wrappedObject.isItem()) {
            throw new UnsupportedOperationException("BukkitItemTypeHolder can wrap only item types!!!");
        }
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public short forcedDurability() {
        return forcedDurability;
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.getMaxStackSize();
    }

    @Override
    public @NotNull ItemTypeHolder withForcedDurability(short durability) {
        var holder = new BukkitItemTypeHolder(wrappedObject);
        holder.forcedDurability = durability;
        return holder;
    }

    @Override
    public @Nullable BlockTypeHolder block() {
        if (!wrappedObject.isBlock()) {
            return null;
        }
        return new BukkitBlockTypeHolder(wrappedObject);
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        NamespacedMappingKey key;
        if (tag instanceof NamespacedMappingKey) {
            key = (NamespacedMappingKey) tag;
        } else {
            key = NamespacedMappingKey.of(tag.toString());
        }
        // native tags
        var bukkitTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, new NamespacedKey(key.namespace(), key.value()), Material.class);
        if (bukkitTag != null) {
            return bukkitTag.isTagged(wrappedObject);
        }
        // backported tags
        if (!key.namespace().equals("minecraft")) {
            return false;
        }
        var value = key.value();
        return BukkitItemTypeMapper.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Material || object instanceof ItemTypeHolder) {
            return equals(object);
        }
        if (object instanceof String) {
            var str = (String) object;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(ItemTypeHolder.ofNullable(object));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == ItemStack.class) {
            ItemStack stack = new ItemStack(wrappedObject);
            if (forcedDurability != 0) {
                // it's somehow still supported because people can be stupid sometimes
                ItemMeta meta = stack.getItemMeta();
                if (meta instanceof Damageable) {
                    ((Damageable) meta).setDamage(forcedDurability);
                    stack.setItemMeta(meta);
                }
            }
            return (T) stack;
        }
        return super.as(type);
    }
}
