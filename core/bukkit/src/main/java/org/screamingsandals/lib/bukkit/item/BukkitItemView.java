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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemStackView;

public class BukkitItemView extends BukkitItem implements ItemStackView {
    public BukkitItemView(@NotNull org.bukkit.inventory.ItemStack stack) {
        super(stack);
    }

    @Override
    public void replace(@Nullable ItemStack replaceable) {
        if (replaceable == null) {
            wrappedObject.setType(Material.AIR);
            return;
        }
        var stack = replaceable.as(org.bukkit.inventory.ItemStack.class);
        wrappedObject.setType(stack.getType());
        wrappedObject.setAmount(stack.getAmount());
        if (!Version.isVersion(1, 13)) {
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
