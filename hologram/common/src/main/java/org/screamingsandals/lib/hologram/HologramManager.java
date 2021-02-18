package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.function.Supplier;

@AbstractService
public abstract class HologramManager {
    private static HologramManager manager;
    protected final Map<UUID, Hologram> activeHolograms = new HashMap<>();

    public HologramManager(Controllable controllable) {
        controllable.disable(this::destroy);
    }

    public static void init(Supplier<HologramManager> supplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("HologramManager is already initialized");
        }
        manager = supplier.get();
    }

    public static Map<UUID, Hologram> getActiveHolograms() {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeHolograms);
    }

    public static Optional<Hologram> getHologram(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        return Optional.ofNullable(getActiveHolograms().get(uuid));
    }

    public static void addHologram(Hologram hologram) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        manager.activeHolograms.put(hologram.getUuid(), hologram);
    }

    public static void removeHologram(UUID uuid) {
        getHologram(uuid).ifPresent(HologramManager::removeHologram);
    }

    public static void removeHologram(Hologram hologram) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        manager.activeHolograms.remove(hologram.getUuid());
        hologram.destroy();
    }

    public static TextHologram textHologram(LocationHolder holder) {
        return textHologram(UUID.randomUUID(), holder, false);
    }

    public static TextHologram textHologram(UUID uuid, LocationHolder holder) {
        return textHologram(uuid, holder, false);
    }

    public static TextHologram textHologram(UUID uuid, LocationHolder holder, boolean touchable) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        final var hologram = manager.textHologram0(uuid, holder, touchable);
        addHologram(hologram);
        return hologram;
    }

    protected abstract TextHologram textHologram0(UUID uuid, LocationHolder holder, boolean touchable);

    protected void destroy() {
        getActiveHolograms().values().forEach(Hologram::destroy);
    }

}
