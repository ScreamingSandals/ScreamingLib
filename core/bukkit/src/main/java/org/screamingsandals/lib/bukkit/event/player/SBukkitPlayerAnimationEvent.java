package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerAnimationEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerAnimationEvent implements SPlayerAnimationEvent, BukkitCancellable {
    private final PlayerAnimationEvent event;

    // Internal cache
    private PlayerWrapper player;
    private PlayerAnimationType animationType;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public PlayerAnimationType getAnimationType() {
        if (animationType == null) {
            animationType = PlayerAnimationType.convert(event.getAnimationType().name());
        }
        return animationType;
    }
}
