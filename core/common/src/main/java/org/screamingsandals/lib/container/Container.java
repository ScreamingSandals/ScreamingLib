package org.screamingsandals.lib.container;

import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;
import java.util.Optional;

public interface Container extends Openable, Wrapper {
    Optional<Item> getItem(int index);

    void setItem(int index, Item item);

    List<Item> addItem(Item... items);

    List<Item> removeItem(Item... items);

    Item[] getContents();

    Item[] getStorageContents();

    void setContents(Item[] items) throws IllegalArgumentException;

    void setStorageContents(Item[] items) throws IllegalArgumentException;

    boolean contains(ItemTypeHolder materialHolder);

    boolean contains(Item item);

    boolean containsAtLeast(Item item, int amount);

    int getSize();

    boolean isEmpty();

    InventoryTypeHolder getType();

    void clear();

    int firstEmptySlot();
}
