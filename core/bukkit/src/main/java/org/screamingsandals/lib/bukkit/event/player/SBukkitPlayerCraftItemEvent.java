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

package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.inventory.CraftItemEvent;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.BasicWrapper;

public class SBukkitPlayerCraftItemEvent extends SBukkitPlayerInventoryClickEvent implements SPlayerCraftItemEvent {

    public SBukkitPlayerCraftItemEvent(CraftItemEvent event) {
        super(event);
    }

    // Internal cache
    private Recipe recipe;

    @Override
    public Recipe recipe() {
        if (recipe == null) {
            recipe = new BukkitRecipe(event().getRecipe());
        }
        return recipe;
    }

    @Override
    public CraftItemEvent event() {
        return (CraftItemEvent) super.event();
    }

    // TODO: Proper Recipe API
    public static class BukkitRecipe extends BasicWrapper<org.bukkit.inventory.Recipe> implements Recipe {

        public BukkitRecipe(org.bukkit.inventory.Recipe wrappedObject) {
            super(wrappedObject);
        }

        @Override
        public Item result() {
            return new BukkitItem(wrappedObject.getResult());
        }
    }
}
