package org.screamingsandals.lib.hologram;

import lombok.Getter;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractHologram implements Hologram {
    protected final int DEFAULT_VIEW_DISTANCE = 4096;
    protected final List<PlayerWrapper> viewers = new LinkedList<>();
    @Getter
    protected final UUID uuid;

    protected LocationHolder location;
    protected int viewDistance;
    protected boolean touchable;
    protected boolean visible;

    protected AbstractHologram(UUID uuid, LocationHolder location, boolean touchable) {
        this.uuid = uuid;
        this.location = location;
        this.touchable = touchable;

        //default values
        this.viewDistance = DEFAULT_VIEW_DISTANCE;
        this.visible = false;
    }

    @Override
    public List<PlayerWrapper> getViewers() {
        return List.copyOf(viewers);
    }

    @Override
    public Hologram addViewer(PlayerWrapper player) {
        if (!viewers.contains(player)) {
            viewers.add(player);
            onViewerAdded(player, true);
        }
        return this;
    }

    @Override
    public Hologram removeViewer(PlayerWrapper player) {
        if (viewers.contains(player)) {
            viewers.remove(player);
            onViewerRemoved(player, false);
        }
        return this;
    }

    @Override
    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    @Override
    public Optional<LocationHolder> getLocation() {
        return Optional.ofNullable(location);
    }

    @Override
    public Hologram setLocation(LocationHolder location) {
        this.location = location;
        update();
        return this;
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public Hologram setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @Override
    public Hologram setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }

    @Override
    public boolean isShown() {
        return visible;
    }

    @Override
    public Hologram show() {
        visible = true;
        update();
        return this;
    }

    @Override
    public Hologram hide() {
        visible = false;
        update();
        return this;
    }

    public abstract void onViewerAdded(PlayerWrapper player, boolean checkDistance);

    public abstract void onViewerRemoved(PlayerWrapper player, boolean checkDistance);

    public abstract void update();
}
