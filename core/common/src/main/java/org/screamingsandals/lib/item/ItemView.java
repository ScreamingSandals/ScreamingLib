package org.screamingsandals.lib.item;

import org.screamingsandals.lib.utils.Replaceable;

/**
 * Represents an item view, not the item itself.
 *
 * You are able to change the viewed item of this view. If the current platform supports mutability of items, the old item will be updated.
 */
// TODO: find all events that can use ItemView instead of Item (Bukkit sometimes doesn't offer setStack method but the ItemStack is mutable and the vanilla one is changed)
public interface ItemView extends Item, Replaceable<Item> {
    void changeAmount(int amount);
}
