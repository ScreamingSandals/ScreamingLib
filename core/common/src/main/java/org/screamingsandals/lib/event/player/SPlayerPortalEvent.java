package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class SPlayerPortalEvent extends SPlayerTeleportEvent {
    private final ObjectLink<Integer> searchRadius;
    private final ObjectLink<Boolean> canCreatePortal;
    private final ObjectLink<Integer> creationRadius;

    public SPlayerPortalEvent(
            ImmutableObjectLink<PlayerWrapper> player,
            ObjectLink<LocationHolder> currentLocation,
            ObjectLink<LocationHolder> newLocation,
            ImmutableObjectLink<TeleportCause> cause,
            ObjectLink<Integer> searchRadius,
            ObjectLink<Boolean> canCreatePortal,
            ObjectLink<Integer> creationRadius
            ) {
        super(player, currentLocation, newLocation, cause);
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    public int getSearchRadius() {
        return searchRadius.get();
    }

    public void setSearchRadius(int getSearchRadius) {
        this.searchRadius.set(getSearchRadius);
    }

    public boolean isCanCreatePortal() {
        return canCreatePortal.get();
    }

    public void setCanCreatePortal(boolean canCreatePortal) {
        this.canCreatePortal.set(canCreatePortal);
    }

    public int getCreationRadius() {
        return creationRadius.get();
    }

    public void setCreationRadius(int creationRadius) {
        this.creationRadius.set(creationRadius);
    }
}
