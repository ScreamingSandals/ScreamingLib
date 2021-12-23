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
    public Recipe getRecipe() {
        if (recipe == null) {
            recipe = new BukkitRecipe(getEvent().getRecipe());
        }
        return recipe;
    }

    @Override
    public CraftItemEvent getEvent() {
        return (CraftItemEvent) super.getEvent();
    }

    // TODO: Proper Recipe API
    public static class BukkitRecipe extends BasicWrapper<org.bukkit.inventory.Recipe> implements Recipe {

        public BukkitRecipe(org.bukkit.inventory.Recipe wrappedObject) {
            super(wrappedObject);
        }

        @Override
        public Item getResult() {
            return new BukkitItem(wrappedObject.getResult());
        }
    }
}
