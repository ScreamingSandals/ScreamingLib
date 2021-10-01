package org.screamingsandals.lib.npc;

import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AbstractService
public abstract class NPCManager extends AbstractVisualsManager<NPC> {
    private static NPCManager manager = null;

    public NPCManager() {
        if (manager != null) {
            throw new UnsupportedOperationException("NPCManager has already been initialized!");
        }
        manager = this;
    }

    public static boolean isInitialized() {
        return manager != null;
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
        manager.addVisual(npc.getUUID(), npc);
    }

    public static void removeNPC(UUID uuid) {
        getNPC(uuid).ifPresent(NPCManager::removeNPC);
    }

    public static void removeNPC(NPC npc) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }
        manager.removeVisual(npc.getUUID());
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
