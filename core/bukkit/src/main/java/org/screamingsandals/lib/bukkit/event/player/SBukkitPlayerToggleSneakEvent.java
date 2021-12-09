package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerToggleSneakEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerToggleSneakEvent implements SPlayerToggleSneakEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerToggleSneakEvent event;

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
    public boolean isSneaking() {
        return event.isSneaking();
    }
}
