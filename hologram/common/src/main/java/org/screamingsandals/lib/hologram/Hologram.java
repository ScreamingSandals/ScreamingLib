package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.Optional;

public interface Hologram {

    List<PlayerWrapper> getViewers();

    Hologram addViewer(PlayerWrapper player);

    Hologram removeViewer(PlayerWrapper player);

    boolean hasViewers();

    Optional<LocationHolder> getLocation();

    Hologram setLocation(LocationHolder location);

    int getViewDistance();

    Hologram setViewDistance(int viewDistance);

    boolean isTouchable();

    Hologram setTouchable(boolean touchable);

    boolean isShown();

    Hologram show();

    Hologram hide();
}
