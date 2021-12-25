package org.screamingsandals.lib.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;
import java.util.Optional;

/**
 * <p>An interface representing an inventory.</p>
 */
public interface Container extends Openable, Wrapper {
    /**
     * <p>Gets the item at the specified slot in this inventory.</p>
     *
     * @param index the inventory slot index
     * @return the item, empty if there is no item
     */
    Optional<Item> getItem(int index);

    /**
     * <p>Sets an item at the specified slot index in this inventory.</p>
     *
     * @param index the inventory slot index
     * @param item the item
     */
    void setItem(int index, Item item);

    /**
     * <p>Tries to add items to empty slots in this inventory.</p>
     *
     * @param items the items
     * @return the items that didn't fit the inventory
     */
    List<Item> addItem(Item... items);

    /**
     * <p>Tries to remove the supplied items from this inventory.</p>
     *
     * @param items the items
     * @return the items that couldn't be removed
     */
    List<Item> removeItem(Item... items);

    /**
     * <p>Returns all items in this inventory.</p>
     * <p>Individual items may be null.</p>
     *
     * @return contents of this inventory
     */
    @Nullable Item @NotNull [] getContents();

    /**
     * <p>Returns the contents from the section of this inventory where items can be stored.</p>
     * <p>In most cases, this will mean the entire inventory. Individual items may be null.</p>
     *
     * @return storage contents of this inventory
     */
    @Nullable Item @NotNull [] getStorageContents();

    /**
     * <p>Sets the complete contents of this inventory to the supplied items.</p>
     *
     * @param items the items
     * @throws IllegalArgumentException when the size of the items array doesn't match the inventory size
     */
    void setContents(@Nullable Item @NotNull [] items) throws IllegalArgumentException;

    /**
     * <p>Sets the contents of the storable section of this inventory to the supplied items.</p>
     *
     * @param items the items
     * @throws IllegalArgumentException when the size of the items array doesn't match the inventory size
     */
    void setStorageContents(@Nullable Item @NotNull [] items) throws IllegalArgumentException;

    /**
     * <p>Checks if the supplied item type is present in this inventory.</p>
     *
     * @param materialHolder the item type
     * @return is the supplied item type present in this inventory?
     */
    boolean contains(ItemTypeHolder materialHolder);

    /**
     * <p>Checks if the supplied item is present in this inventory.</p>
     *
     * @param item the item
     * @return is the supplied item present in this inventory?
     */
    boolean contains(Item item);

    /**
     * <p>Checks if the amount of occurrences of the supplied item in this inventory is higher than or equal to the supplied amount.</p>
     *
     * @param item the item
     * @param amount the amount
     * @return is the amount of occurrences at least the supplied amount?
     */
    boolean containsAtLeast(Item item, int amount);

    /**
     * <p>Gets the size of this inventory.</p>
     *
     * @return the size of this inventory
     */
    int getSize();

    /**
     * <p>Checks if this inventory is empty.</p>
     *
     * @return is this inventory empty?
     */
    boolean isEmpty();

    /**
     * <p>Gets the type of this inventory.</p>
     *
     * @return the type of this inventory
     */
    InventoryTypeHolder getType();

    /**
     * <p>Completely wipes the contents of this inventory.</p>
     */
    void clear();

    /**
     * <p>Gets the index of the first empty slot in this inventory.</p>
     *
     * @return the index of the first empty slot, -1 if there is no empty slot
     */
    int firstEmptySlot();
}
