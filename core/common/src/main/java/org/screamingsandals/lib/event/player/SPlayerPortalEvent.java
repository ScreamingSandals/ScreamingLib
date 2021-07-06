package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class SPlayerPortalEvent extends SPlayerTeleportEvent {
    private int getSearchRadius = 128;
    private boolean canCreatePortal = true;
    private int creationRadius = 16;

    public SPlayerPortalEvent(PlayerWrapper player, LocationHolder currentLocation, LocationHolder newLocation, TeleportCause cause) {
        super(player, currentLocation, newLocation, cause);
    }
}
