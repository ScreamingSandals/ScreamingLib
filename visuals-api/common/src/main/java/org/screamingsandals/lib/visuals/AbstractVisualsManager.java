package org.screamingsandals.lib.visual;

import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.visuals.Visual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractVisualsManager<T extends LocatableVisual<T>> {
    private final Map<UUID, T> activeVisuals = new ConcurrentHashMap<>();

    protected AbstractVisualsManager(Controllable controllable) {
        controllable.disable(this::destroy);
    }

    public void addVisual(UUID uuid, T visual) {
        activeVisuals.put(uuid, visual);
    }

    public void removeVisual(T visual) {
        removeVisual(visual.getUuid());
    }

    public void removeVisual(UUID uuid) {
        activeVisuals.remove(uuid);
    }

    public Map<UUID, T> getActiveVisuals() {
        return Map.copyOf(activeVisuals);
    }

    protected void destroy() {
        Map.copyOf(getActiveVisuals())
                .values()
                .forEach(Visual::destroy);
        activeVisuals.clear();
    }

    public abstract T createVisual(UUID uuid, LocationHolder holder, boolean touchable);
}
