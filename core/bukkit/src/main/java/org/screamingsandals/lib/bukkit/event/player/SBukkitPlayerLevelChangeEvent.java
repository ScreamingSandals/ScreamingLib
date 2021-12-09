package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerLevelChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerLevelChangeEvent implements SPlayerLevelChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerLevelChangeEvent event;

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
    public int getOldLevel() {
        return event.getOldLevel();
    }

    @Override
    public int getNewLevel() {
        return event.getNewLevel();
    }
}
