package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.screamingsandals.lib.bukkit.event.entity.SBukkitProjectileLaunchEvent;
import org.screamingsandals.lib.event.player.SPlayerProjectileLaunchEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public class SBukkitPlayerProjectileLaunchEvent extends SBukkitProjectileLaunchEvent implements SPlayerProjectileLaunchEvent {
    public SBukkitPlayerProjectileLaunchEvent(ProjectileLaunchEvent event) {
        super(event);
    }

    @Override
    public PlayerWrapper getPlayer() {
        return (PlayerWrapper) getShooter();
    }
}
