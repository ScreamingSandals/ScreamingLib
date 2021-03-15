package org.screamingsandals.lib.hologram;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractHologram implements Hologram {
    protected ConcurrentSkipListMap<Integer, Component> lines = new ConcurrentSkipListMap<>();
    protected final List<PlayerWrapper> viewers = new CopyOnWriteArrayList<>();
    @Getter
    protected final UUID uuid;

    protected Integer originalLinesSize = 0;
    protected LocationHolder location;
    protected int viewDistance;
    protected boolean touchable;
    protected boolean visible;
    protected boolean ready = false;
    protected Data data;
    protected float rotationIncrement;
    protected Pair<Integer, TaskerTime> rotationTime;
    protected RotationMode rotationMode = RotationMode.NONE;
    protected Item item;
    protected ItemLocation itemLocation = ItemLocation.ABOVE;

    protected AbstractHologram(UUID uuid, LocationHolder location, boolean touchable) {
        this.uuid = uuid;
        this.location = location;
        this.touchable = touchable;

        //default values
        this.viewDistance = DEFAULT_VIEW_DISTANCE;
        this.rotationIncrement = DEFAULT_ROTATION_INCREMENT;
        this.visible = false;
        this.data = new SimpleData();
        this.rotationTime = Pair.of(2, TaskerTime.TICKS);
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
    public Hologram removeViewer(PlayerWrapper player, boolean sendPackets) {
        if (viewers.contains(player)) {
            viewers.remove(player);

            if (sendPackets) {
                onViewerRemoved(player, false);
            }
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

        ready = true;
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
        ready = false;
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
        hide();
        viewers.clear();

        HologramManager.removeHologram(this);
    }

    @Override
    public Map<Integer, Component> getLines() {
        return new ConcurrentSkipListMap<>(lines);
    }

    @Override
    public Hologram firstLine(Component text) {
        return setLine(0, text);
    }

    @Override
    public Hologram newLine(List<Component> text) {
        text.forEach(this::newLine);
        return this;
    }

    @Override
    public Hologram newLine(Component text) {
        if (lines.isEmpty()) {
            return firstLine(text);
        }

        originalLinesSize = lines.size();
        lines.put(lines.lastKey() + 1, text);
        update();
        return this;
    }

    @Override
    public Hologram addLine(int line, Component text) {
        originalLinesSize = lines.size();
        lines = HologramUtils.addEntryAndMoveRest(lines, line, text);
        update();
        return this;
    }

    @Override
    public Hologram setLine(int line, Component text) {
        if (!lines.containsKey(line)) {
            return addLine(line, text);
        }
        originalLinesSize = lines.size();
        lines.put(line, text);
        update();
        return this;
    }

    @Override
    public Hologram removeLine(int line) {
        originalLinesSize = lines.size();
        lines = HologramUtils.removeEntryAndMoveRest(lines, line);
        update();
        return this;
    }

    @Override
    public Hologram replaceLines(Map<Integer, Component> lines) {
        originalLinesSize = lines.size();
        this.lines = new ConcurrentSkipListMap<>(lines);
        update();
        return this;
    }

    @Override
    public Hologram replaceLines(List<Component> lines) {
        final var toSet = new TreeMap<Integer, Component>();
        for (int i = 0; i < lines.size(); i++) {
            toSet.put(i, lines.get(i));
        }
        return replaceLines(toSet);
    }

    @Override
    public Pair<Integer, TaskerTime> getRotationTime() {
        return rotationTime;
    }

    @Override
    public Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime) {
        this.rotationTime = rotatingTime;
        update();
        return this;
    }

    @Override
    public Hologram rotationMode(RotationMode mode) {
        this.rotationMode = mode;
        update();
        return this;
    }

    @Override
    public RotationMode getRotationMode() {
        return rotationMode;
    }

    @Override
    public Hologram item(Item item) {
        this.item = item;
        update();
        return this;
    }

    @Override
    public Hologram rotationIncrement(float toIncrement) {
        this.rotationIncrement = toIncrement;
        return this;
    }

    @Override
    public Hologram itemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
        update();
        return this;
    }

    @Override
    public ItemLocation getItemLocation() {
        return this.itemLocation;
    }

    public abstract void onViewerAdded(PlayerWrapper player, boolean checkDistance);

    public abstract void onViewerRemoved(PlayerWrapper player, boolean checkDistance);

    protected abstract void update0();

    public void update() {
        if (ready) {
            update0();
        }
    }

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
