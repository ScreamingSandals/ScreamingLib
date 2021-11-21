package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractAtEntityEvent;

public class SBukkitPlayerInteractAtEntityEvent extends SBukkitPlayerInteractEntityEvent implements SPlayerInteractAtEntityEvent {
    public SBukkitPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        super(event);
    }
}
