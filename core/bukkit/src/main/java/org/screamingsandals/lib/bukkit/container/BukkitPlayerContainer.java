package org.screamingsandals.lib.bukkit.container;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.material.builder.BukkitItemFactory;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.container.PlayerContainer;

import java.util.Arrays;

public class BukkitPlayerContainer extends BukkitContainer implements PlayerContainer {
    public BukkitPlayerContainer(PlayerInventory wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Item[] getArmorContents() {
        return Arrays.stream(((PlayerInventory)wrappedObject)
                .getArmorContents())
                .map(item -> BukkitItemFactory.build(item).orElse(null))
                .toArray(Item[]::new);
    }

    @Override
    public @Nullable Item getHelmet() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getHelmet())
                .orElse(null);
    }

    @Override
    public @Nullable Item getChestplate() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getChestplate())
                .orElse(null);
    }

    @Override
    public @Nullable Item getLeggings() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getLeggings())
                .orElse(null);
    }

    @Override
    public @Nullable Item getBoots() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getBoots())
                .orElse(null);
    }

    @Override
    public void setArmorContents(@Nullable Item[] items) {
        if (items == null) {
            ((PlayerInventory)wrappedObject).setArmorContents(null);
            return;
        }
        ((PlayerInventory)wrappedObject).setArmorContents(Arrays.stream(items).map(item -> {
            if (item == null) {
                return null;
            }
            return item.as(ItemStack.class);
        }).toArray(ItemStack[]::new));
    }


    @Override
    public void setHelmet(@Nullable Item helmet) {
        var inventory = (PlayerInventory) wrappedObject;
        if (helmet == null) {
            inventory.setHelmet(null);
            return;
        }
        inventory.setHelmet(helmet.as(ItemStack.class));
    }

    @Override
    public void setChestplate(@Nullable Item chestplate) {
        var inventory = (PlayerInventory) wrappedObject;
        if (chestplate == null) {
            inventory.setChestplate(null);
            return;
        }
        inventory.setChestplate(chestplate.as(ItemStack.class));
    }

    @Override
    public void setLeggings(@Nullable Item leggings) {
        var inventory = (PlayerInventory) wrappedObject;
        if (leggings == null) {
            inventory.setLeggings(null);
            return;
        }
        inventory.setLeggings(leggings.as(ItemStack.class));
    }

    @Override
    public void setBoots(@Nullable Item boots) {
        var inventory = (PlayerInventory) wrappedObject;
        if (boots == null) {
            inventory.setBoots(null);
            return;
        }
        inventory.setBoots(boots.as(ItemStack.class));
    }

    @Override
    public @NotNull Item getItemInMainHand() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getItemInMainHand())
                .orElseThrow();
    }

    @Override
    public void setItemInMainHand(@Nullable Item item) {
        var inventory = (PlayerInventory) wrappedObject;
        if (item == null) {
            inventory.setItemInMainHand(null);
            return;
        }
        inventory.setItemInMainHand(item.as(ItemStack.class));
    }

    @Override
    public @NotNull Item getItemInOffHand() {
        return BukkitItemFactory
                .build(((PlayerInventory)wrappedObject).getItemInOffHand())
                .orElseThrow();
    }

    @Override
    public void setItemInOffHand(@Nullable Item item) {
        var inventory = (PlayerInventory) wrappedObject;
        if (item == null) {
            inventory.setItemInOffHand(null);
            return;
        }
        inventory.setItemInOffHand(item.as(ItemStack.class));
    }

    @Override
    public int getHeldItemSlot() {
        return ((PlayerInventory)wrappedObject).getHeldItemSlot();
    }

    @Override
    public void setHeldItemSlot(int slot) {
        ((PlayerInventory)wrappedObject).setHeldItemSlot(slot);
    }
}
