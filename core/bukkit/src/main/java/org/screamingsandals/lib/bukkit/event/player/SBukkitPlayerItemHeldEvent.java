package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerItemHeldEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerItemHeldEvent implements SPlayerItemHeldEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerItemHeldEvent event;

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
    public int getPreviousSlot() {
        return event.getPreviousSlot();
    }

    @Override
    public int getNewSlot() {
        return event.getNewSlot();
    }
}
