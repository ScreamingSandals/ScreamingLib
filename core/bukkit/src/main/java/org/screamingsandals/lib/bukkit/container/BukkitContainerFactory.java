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

package org.screamingsandals.lib.bukkit.container;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service
public class BukkitContainerFactory extends ContainerFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Container> Optional<C> wrapContainer0(Object container) {
        if (container instanceof PlayerInventory) {
            return (Optional<C>) Optional.of(new BukkitPlayerContainer((PlayerInventory) container));
        }

        if (container instanceof Inventory) {
            return (Optional<C>) Optional.of(new BukkitContainer((Inventory) container));
        }
        return Optional.empty();
    }

    @Override
    public <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type) {
        return wrapContainer0(Bukkit.createInventory(null, type.as(InventoryType.class)));
    }

    @Override
    public <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type, Component name) {
        var container = AdventureUtils
                .get(Bukkit.getServer(), "createInventory", InventoryHolder.class, InventoryType.class, Component.class)
                .ifPresentOrElseGet(classMethod ->
                                classMethod
                                        .invokeInstanceResulted(Bukkit.getServer(), null, type.as(InventoryType.class), name)
                                        .as(Inventory.class),
                        () -> Bukkit.createInventory(null, type.as(InventoryType.class), AdventureHelper.toLegacy(name)));
        return wrapContainer0(container);
    }

    @Override
    public <C extends Container> Optional<C> createContainer0(int size) {
        return wrapContainer0(Bukkit.createInventory(null, size));
    }

    @Override
    public <C extends Container> Optional<C> createContainer0(int size, Component name) {
        var container = AdventureUtils
                .get(Bukkit.getServer(), "createInventory", InventoryHolder.class, int.class, Component.class)
                .ifPresentOrElseGet(classMethod ->
                                classMethod
                                        .invokeInstanceResulted(Bukkit.getServer(), null, size, name)
                                        .as(Inventory.class),
                        () -> Bukkit.createInventory(null, size, AdventureHelper.toLegacy(name)));
        return wrapContainer0(container);
    }
}
