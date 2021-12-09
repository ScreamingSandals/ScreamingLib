package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerExpChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerExpChangeEvent implements SPlayerExpChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerExpChangeEvent event;

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
    public int getExp() {
        return event.getAmount();
    }

    @Override
    public void setExp(int exp) {
        event.setAmount(exp);
    }
}
