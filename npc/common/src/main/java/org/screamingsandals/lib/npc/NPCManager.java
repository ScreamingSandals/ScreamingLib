package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.hologram.Hologram;
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
public abstract class NPCManager extends AbstractVisualsManager<NPC> {
    private static AbstractVisualsManager<NPC> manager = null;

    protected NPCManager(Controllable controllable) {
        super(controllable);
    }

    public static boolean isInitialized() {
        return manager != null;
    }

    public static void init(@NotNull Supplier<AbstractVisualsManager<NPC>> packetMapperSupplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("NPCManager is already initialized.");
        }
        manager = packetMapperSupplier.get();
    }

    public static Map<UUID, NPC> getActiveNPCS() {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        return manager.getActiveVisuals();
    }

    public static Optional<NPC> getNPC(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        return Optional.ofNullable(getActiveNPCS().get(uuid));
    }

    public static void addNPC(NPC npc) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        manager.getActiveVisuals().put(npc.getUUID(), npc);
    }

    public static void removeHologram(UUID uuid) {
        getNPC(uuid).ifPresent(NPCManager::removeNPC);
    }

    public static void removeNPC(NPC npc) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        manager.getActiveVisuals().remove(npc.getUUID());
    }

    public static NPC npc(LocationHolder holder) {
        return npc(UUID.randomUUID(), holder, true);
    }

    public static NPC npc(UUID uuid, LocationHolder holder, boolean touchable) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        final var npc = manager.createVisual(uuid, holder, touchable);
        addNPC(npc);
        return npc;
    }
}
