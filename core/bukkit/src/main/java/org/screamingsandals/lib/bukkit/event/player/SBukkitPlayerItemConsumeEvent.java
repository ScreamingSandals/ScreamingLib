package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerItemConsumeEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerItemConsumeEvent implements SPlayerItemConsumeEvent, BukkitCancellable {
    private final PlayerItemConsumeEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable Item getItem() {
        return event.getItem() != null ? ItemFactory.build(event.getItem()).orElseThrow() : null;
    }

    @Override
    public void setItem(@Nullable Item item) {
        event.setItem(item != null ? item.as(ItemStack.class) : null);
    }
}
