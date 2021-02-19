package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

public interface OfflinePlayerWrapper extends MultiPlatformOfflinePlayer {
    Optional<LocationHolder> getBedLocation();
}
