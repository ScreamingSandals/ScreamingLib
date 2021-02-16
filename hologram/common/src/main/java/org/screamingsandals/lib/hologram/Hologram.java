package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Hologram {
    int DEFAULT_VIEW_DISTANCE = 4096;

    UUID getUuid();

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

    void destroy();

    Data getData();

    void newData(Data data);

    /**
     * Data storage for given hologram
     */
    interface Data {
        Map<String, Object> getAll();

        <T> T get(String key);

        boolean contains(String key);

        void set(String key, Object data);

        void add(String key, Object data);
    }
}
