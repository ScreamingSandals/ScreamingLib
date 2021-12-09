package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityItem;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.player.SPlayerDropItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerDropItemEvent implements SPlayerDropItemEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerDropItemEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityItem itemDrop;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public EntityItem getItemDrop() {
        if (itemDrop == null) {
            itemDrop = new BukkitEntityItem(event.getItemDrop());
        }
        return itemDrop;
    }
}
