package org.screamingsandals.lib.hologram;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;

public abstract class AbstractHologram implements Hologram {
    protected final List<PlayerWrapper> viewers = new LinkedList<>();
    @Getter
    protected final UUID uuid;

    protected LocationHolder location;
    protected int viewDistance;
    protected boolean touchable;
    protected boolean visible;
    protected Data data;

    protected AbstractHologram(UUID uuid, LocationHolder location, boolean touchable) {
        this.uuid = uuid;
        this.location = location;
        this.touchable = touchable;

        //default values
        this.viewDistance = DEFAULT_VIEW_DISTANCE;
        this.visible = false;
        this.data = new SimpleData();
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
        if (isShown()) {
            return this;
        }

        visible = true;
        update();
        return this;
    }

    @Override
    public Hologram hide() {
        if (!isShown()) {
            return this;
        }

        visible = false;
        update();
        return this;
    }

    @Override
    public Data getData() {
        return data;
    }

    @Override
    public void newData(@NotNull Data data) {
        this.data = data;
    }

    @Override
    public void destroy() {
        data = null;
        viewers.clear();
        HologramManager.removeHologram(this);
    }

    public abstract void onViewerAdded(PlayerWrapper player, boolean checkDistance);

    public abstract void onViewerRemoved(PlayerWrapper player, boolean checkDistance);

    public abstract void update();

    public static class SimpleData implements Data {
        private final Map<String, Object> dataMap = new HashMap<>();

        @Override
        public Map<String, Object> getAll() {
            return Map.copyOf(dataMap);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String key) {
            if (dataMap.containsKey(key)) {
                return (T) dataMap.get(key);
            }

            throw new NullPointerException("Data for key " + key + " was not found!");
        }

        @Override
        public boolean contains(String key) {
            return dataMap.containsKey(key);
        }

        @Override
        public void set(String key, Object data) {
            dataMap.put(key, data);
        }

        @Override
        public void add(String key, Object data) {
            dataMap.putIfAbsent(key, data);
        }
    }
}
