package org.screamingsandals.lib.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.Item;

/**
 * <p>An interface representing a player's inventory.</p>
 *
 * <p>A visualization of slot indexes in a player inventory:</p>
 * <img src="https://i.imgur.com/wZ1B8jC.png">
 */
public interface PlayerContainer extends Container {
    /**
     * <p>Gets the contents of the armor slots.</p>
     *
     * @return the armor items
     */
    @Nullable Item @NotNull [] getArmorContents();

    /**
     * <p>Gets the item in the helmet slot.</p>
     *
     * @return the item in the helmet slot, null if there is no item
     */
    @Nullable
    Item getHelmet();

    /**
     * <p>Gets the item in the chestplate slot.</p>
     *
     * @return the item in the chestplate slot, null if there is no item
     */
    @Nullable
    Item getChestplate();

    /**
     * <p>Gets the item in the leggings slot.</p>
     *
     * @return the item in the leggings slot, null if there is no item
     */
    @Nullable
    Item getLeggings();

    /**
     * <p>Gets the item in the boots slot.</p>
     *
     * @return the item in the boots slot, null if there is no item
     */
    @Nullable
    Item getBoots();

    /**
     * <p>Sets the armor slots to the supplied items.</p>
     *
     * @param items the armor items
     */
    void setArmorContents(@Nullable Item @Nullable [] items);

    /**
     * <p>Sets the helmet slot to the supplied item.</p>
     *
     * @param helmet the item
     */
    void setHelmet(@Nullable Item helmet);

    /**
     * <p>Sets the chestplate slot to the supplied item.</p>
     *
     * @param chestplate the item
     */
    void setChestplate(@Nullable Item chestplate);

    /**
     * <p>Sets the leggings slot to the supplied item.</p>
     *
     * @param leggings the item
     */
    void setLeggings(@Nullable Item leggings);

    /**
     * <p>Sets the boots slot to the supplied item.</p>
     *
     * @param boots the item
     */
    void setBoots(@Nullable Item boots);

    /**
     * <p>Gets a copy of the item in the main hand slot.</p>
     *
     * @return the item
     */
    @NotNull
    Item getItemInMainHand();

    /**
     * <p>Sets the main hand slot to the supplied item.</p>
     *
     * @param item the item
     */
    void setItemInMainHand(@Nullable Item item);

    /**
     * <p>Gets a copy of the item in the offhand slot.</p>
     *
     * @return the item
     */
    @NotNull
    Item getItemInOffHand();

    /**
     * <p>Sets the offhand slot to the supplied item.</p>
     *
     * @param item the item
     */
    void setItemInOffHand(@Nullable Item item);

    /**
     * <p>Gets the slot index of the currently held item.</p>
     *
     * @return the slot index
     */
    int getHeldItemSlot();

    /**
     * <p>Sets the slot index of the currently held item.</p>
     *
     * @param slot the new slot index
     */
    void setHeldItemSlot(int slot);
}
