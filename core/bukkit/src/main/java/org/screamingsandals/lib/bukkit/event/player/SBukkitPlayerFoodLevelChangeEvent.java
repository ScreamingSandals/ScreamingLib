package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerFoodLevelChangeEvent implements SPlayerFoodLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final FoodLevelChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;
    private boolean itemCached;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getEntity());
        }
        return player;
    }

    @Override
    public @Nullable Item getItem() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItem(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }

    @Override
    public int getFoodLevel() {
        return event.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        event.setFoodLevel(foodLevel);
    }
}
