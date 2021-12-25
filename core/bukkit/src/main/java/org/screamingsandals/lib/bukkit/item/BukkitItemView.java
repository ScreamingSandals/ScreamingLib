package org.screamingsandals.lib.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemView;
import org.screamingsandals.lib.utils.Platform;

public class BukkitItemView extends BukkitItem implements ItemView {
    public BukkitItemView(ItemStack stack) {
        super(stack);
    }

    @Override
    public void replace(Item replaceable) {
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
