package org.screamingsandals.lib.bukkit.item.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.screamingsandals.lib.bukkit.container.BukkitContainer;
import org.screamingsandals.lib.bukkit.container.BukkitPlayerContainer;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.*;

@Service
public class BukkitItemFactory extends ItemFactory {

    public BukkitItemFactory() {
        itemConverter
                .registerW2P(ItemStack.class, item -> (ItemStack) item.raw())
                .registerP2W(ItemStack.class, BukkitItem::new);
    }

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

    @Override
    protected ItemBuilder builder0() {
        return new BukkitItemBuilder(null);
    }
}
