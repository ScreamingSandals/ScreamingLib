package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerFoodLevelChangeEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<@Nullable Item> item;
    private final ObjectLink<Integer> foodLevel;

    public PlayerWrapper getPlayer() {
        return player.get();
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
