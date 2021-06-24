package org.screamingsandals.lib.material.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;

public interface PlayerContainer extends Container {

    Item[] getArmorContents();

    @Nullable
    Item getHelmet();

    @Nullable
    Item getChestplate();

    @Nullable
    Item getLeggings();

    @Nullable
    Item getBoots();

    void setArmorContents(@Nullable Item[] items);

    void setHelmet(@Nullable Item helmet);

    void setChestplate(@Nullable Item chestplate);

    void setLeggings(@Nullable Item leggings);

    void setBoots(@Nullable Item boots);

    @NotNull
    Item getItemInMainHand();

    void setItemInMainHand(@Nullable Item item);

    @NotNull
    Item getItemInOffHand();

    void setItemInOffHand(@Nullable Item item);

    int getHeldItemSlot();

    void setHeldItemSlot(int slot);
}
