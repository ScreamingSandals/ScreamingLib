package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityItem;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.player.SPlayerPickupItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitLegacyPlayerPickupItemEvent implements SPlayerPickupItemEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerPickupItemEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityItem item;

    @Override
    public EntityBasic getEntity() {
        return getPlayer();
    }

    @Override
    public EntityItem getItem() {
        if (item == null) {
            item = new BukkitEntityItem(event.getItem());
        }
        return item;
    }

    @Override
    public int getRemaining() {
        return event.getRemaining();
    }

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }
}
