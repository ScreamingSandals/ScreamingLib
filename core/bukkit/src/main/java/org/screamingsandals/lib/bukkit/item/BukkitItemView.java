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

package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemView;
import org.screamingsandals.lib.utils.Platform;

public class BukkitItemView extends BukkitItem implements ItemView {
    public BukkitItemView(@NotNull ItemStack stack) {
        super(stack);
    }

    @Override
    public void replace(@Nullable Item replaceable) {
        if (replaceable == null) {
            wrappedObject.setType(Material.AIR);
            return;
        }
        var stack = replaceable.as(ItemStack.class);
        wrappedObject.setType(stack.getType());
        wrappedObject.setAmount(stack.getAmount());
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            wrappedObject.setDurability(stack.getDurability());
        }
        if (stack.hasItemMeta()) {
            wrappedObject.setItemMeta(stack.getItemMeta());
        }
    }

    @Override
    public void changeAmount(int amount) {
        wrappedObject.setAmount(amount);
    }
}
