package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerItemDamageEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerItemDamageEvent implements SPlayerItemDamageEvent, BukkitCancellable {
    private final PlayerItemDamageEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Item getItem() {
        if (item == null) {
            item = ItemFactory.build(event.getItem()).orElseThrow();
        }
        return item;
    }

    @Override
    public int getDamage() {
        return event.getDamage();
    }

    @Override
    public void setDamage(int damage) {
        event.setDamage(damage);
    }
}
