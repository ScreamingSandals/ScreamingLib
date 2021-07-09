package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class NPCManager {
    private static NPCManager manager = null;
    protected final Map<UUID, NPC> activeNPCS = new HashMap<>();

    public static boolean isInitialized() {
        return manager != null;
    }

    public static void init(@NotNull Supplier<NPCManager> packetMapperSupplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("NPCManager is already initialized.");
        }
        manager = packetMapperSupplier.get();
    }

    public static Map<UUID, NPC> getActiveNPCS() {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeNPCS);
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

        manager.activeNPCS.put(npc.getUUID(), npc);
    }

    public static void removeHologram(UUID uuid) {
        getNPC(uuid).ifPresent(NPCManager::removeNPC);
    }

    public static void removeNPC(NPC npc) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }
        manager.activeNPCS.remove(npc.getUUID());
    }

    public static NPC npc(LocationHolder holder) {
        if (manager == null) {
            throw new UnsupportedOperationException("NPCManager is not initialized yet!");
        }

        final var npc = manager.npc0(holder);
        addNPC(npc);
        return npc;
    }

    protected void destroy() {
        Map.copyOf(getActiveNPCS())
                .values()
                .forEach(NPC::destroy);
        manager.activeNPCS.clear();
    }

    public abstract AbstractNPC npc0(LocationHolder location);
}
