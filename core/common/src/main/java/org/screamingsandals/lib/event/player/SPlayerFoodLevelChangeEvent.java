package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerFoodLevelChangeEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<@Nullable Item> item;
    private final ObjectLink<Integer> foodLevel;

    public SPlayerFoodLevelChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                       ImmutableObjectLink<@Nullable Item> item,
                                       ObjectLink<Integer> foodLevel) {
        super(player);
        this.item = item;
        this.foodLevel = foodLevel;
    }

    @Nullable
    public Item getItem() {
        return item.get();
    }

    public int getFoodLevel() {
        return foodLevel.get();
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel.set(foodLevel);
    }
}
