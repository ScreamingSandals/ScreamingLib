package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.Optional;

public interface Hologram {

    List<PlayerWrapper> getViewers();

    void addViewer(PlayerWrapper player);

    void removeViewer(PlayerWrapper player);

    boolean hasViewers();

    Optional<LocationHolder> getLocation();

    void setLocation(LocationHolder location);

    int getViewDistance();

    void setViewDistance(int viewDistance);

    boolean isTouchable();

    List<TouchHandler> getTouchHandlers();

    void addTouchHandler(TouchHandler handler);

    void removeTouchHandler(TouchHandler handler);

    void clearTouchHandlers();

    boolean isVisible();

    void setVisible(boolean visible);

    @FunctionalInterface
    interface TouchHandler {
        void handleTouch(PlayerWrapper who, Hologram clicked);
    }
}
