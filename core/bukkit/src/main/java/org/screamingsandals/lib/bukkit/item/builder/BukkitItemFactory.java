package org.screamingsandals.lib.bukkit.item.builder;

import org.bukkit.inventory.*;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitItemFactory extends ItemFactory {

    public BukkitItemFactory() {
        itemConverter
                .registerW2P(ItemStack.class, item -> (ItemStack) item.raw())
                .registerP2W(ItemStack.class, BukkitItem::new);
    }

    @Override
    protected ItemBuilder builder0() {
        return new BukkitItemBuilder(null);
    }
}
