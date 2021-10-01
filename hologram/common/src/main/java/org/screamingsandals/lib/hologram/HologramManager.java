package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AbstractService
@ServiceDependencies(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        Tasker.class,
        ItemFactory.class,
        PacketMapper.class
})
public abstract class HologramManager extends AbstractVisualsManager<Hologram> {
    private static HologramManager manager = null;

    protected HologramManager() {
        if (manager != null) {
            throw new UnsupportedOperationException("HologramManager is already initialized");
        }
        manager = this;
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
}
