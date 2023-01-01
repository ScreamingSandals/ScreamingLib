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

package org.screamingsandals.lib.bukkit.container;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitContainerFactory extends ContainerFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Container> @Nullable C wrapContainer0(Object container) {
        if (container instanceof PlayerInventory) {
            return (C) new BukkitPlayerContainer((PlayerInventory) container);
        }

        if (container instanceof Inventory) {
            return (C) new BukkitContainer((Inventory) container);
        }
        return null;
    }

    @Override
    public <C extends Container> @Nullable C createContainer0(@Nullable InventoryTypeHolder type) {
        if (type == null) {
            return null;
        }
        return wrapContainer0(Bukkit.createInventory(null, type.as(InventoryType.class)));
    }

    @Override
    public <C extends Container> @Nullable C createContainer0(@Nullable InventoryTypeHolder type, @Nullable Component name) {
        if (type == null) {
            return null;
        }
        if (name == null) {
            return wrapContainer0(type);
        }
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return wrapContainer0(Bukkit.createInventory(null, type.as(InventoryType.class), name.as(net.kyori.adventure.text.Component.class)));
        } else {
            return wrapContainer0(Bukkit.createInventory(null, type.as(InventoryType.class), name.toLegacy()));
        }
    }

    @Override
    public <C extends Container> @Nullable C createContainer0(int size) {
        return wrapContainer0(Bukkit.createInventory(null, size));
    }

    @Override
    public <C extends Container> @Nullable C createContainer0(int size, @Nullable Component name) {
        if (name == null) {
            return wrapContainer0(size);
        }
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return wrapContainer0(Bukkit.createInventory(null, size, name.as(net.kyori.adventure.text.Component.class)));
        } else {
            return wrapContainer0(Bukkit.createInventory(null, size, name.toLegacy()));
        }
    }
}
