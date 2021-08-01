package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.visual.AbstractVisualsManager;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@AbstractService
public abstract class HologramManager extends AbstractVisualsManager<Hologram> {
    private static AbstractVisualsManager<Hologram> manager = null;

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Supplier<AbstractVisualsManager<Hologram>> supplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("HologramManager is already initialized");
        }

        manager = supplier.get();
    }

    public static Hologram hologram(LocationHolder holder) {
        return hologram(UUID.randomUUID(), holder, false);
    }

    public static Hologram hologram(LocationHolder holder, boolean touchable) {
        return hologram(UUID.randomUUID(), holder, touchable);
    }

    public static Hologram hologram(UUID uuid, LocationHolder holder) {
        return hologram(uuid, holder, false);
    }

    public static Hologram hologram(UUID uuid, LocationHolder holder, boolean touchable) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        final var hologram = manager.createVisual(uuid, holder, touchable);
        addHologram(hologram);
        return hologram;
    }

    public static Map<UUID, Hologram> getActiveHolograms() {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        return manager.getActiveVisuals();
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

        manager.addVisual(hologram.getUuid(), hologram);
    }

    public static void removeHologram(UUID uuid) {
        getHologram(uuid).ifPresent(HologramManager::removeHologram);
    }

    public static void removeHologram(Hologram hologram) {
        if (manager == null) {
            throw new UnsupportedOperationException("HologramManager is not initialized yet!");
        }

        manager.removeVisual(hologram.getUuid());
    }

    protected HologramManager(Controllable controllable) {
        super(controllable);
    }
}
