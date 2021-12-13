package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.screamingsandals.lib.event.player.SPlayerTeleportEvent;

public class SBukkitPlayerTeleportEvent extends SBukkitPlayerMoveEvent implements SPlayerTeleportEvent {
    public SBukkitPlayerTeleportEvent(PlayerTeleportEvent event) {
        super(event);
    }

    // Internal cache
    private TeleportCause teleportCause;

    @Override
    public TeleportCause getCause() {
        if (teleportCause == null) {
            teleportCause = TeleportCause.valueOf(getEvent().getCause().name());
        }
        return teleportCause;
    }

    @Override
    public PlayerTeleportEvent getEvent() {
        return (PlayerTeleportEvent) super.getEvent();
    }
}
