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

package org.screamingsandals.lib.impl.bukkit.event.player;

import org.bukkit.event.inventory.CraftItemEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.PlayerCraftItemEvent;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.recipe.Recipe;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BukkitPlayerCraftItemEvent extends BukkitPlayerInventoryClickEvent implements PlayerCraftItemEvent {

    public BukkitPlayerCraftItemEvent(@NotNull CraftItemEvent event) {
        super(event);
    }

    // Internal cache
    private @Nullable Recipe recipe;

    @Override
    public @NotNull Recipe recipe() {
        if (recipe == null) {
            recipe = new BukkitRecipe(event().getRecipe());
        }
        return recipe;
    }

    @Override
    public @NotNull CraftItemEvent event() {
        return (CraftItemEvent) super.event();
    }

    // TODO: Proper Recipe API
    public static class BukkitRecipe extends BasicWrapper<org.bukkit.inventory.Recipe> implements Recipe {

        public BukkitRecipe(org.bukkit.inventory.@NotNull Recipe wrappedObject) {
            super(wrappedObject);
        }

        @Override
        public @NotNull ItemStack result() {
            return new BukkitItem(wrappedObject.getResult());
        }
    }
}
