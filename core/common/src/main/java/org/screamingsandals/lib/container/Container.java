package org.screamingsandals.lib.container;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;
import java.util.Optional;

public interface Container extends Openable, Wrapper {
    Optional<Item> getItem(int index);

    void setItem(int index, Item item);

    List<Item> addItem(Item... items);

    List<Item> removeItem(Item... items);

    Item[] getContents();

    void setContents(Item[] items) throws IllegalArgumentException;

    boolean contains(MaterialHolder materialHolder);

    boolean contains(Item item);

    boolean containsAtLeast(Item item, int amount);

    int getSize();

    boolean isEmpty();
}
