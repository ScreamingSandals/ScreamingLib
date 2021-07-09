package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.function.Supplier;

@AbstractService
public abstract class NPCManager {
    private static NPCManager npcManager = null;

    public static boolean isInitialized() {
        return npcManager != null;
    }

    public static void init(@NotNull Supplier<NPCManager> packetMapperSupplier) {
        if (npcManager != null) {
            throw new UnsupportedOperationException("NPCManager is already initialized.");
        }
        npcManager = packetMapperSupplier.get();
    }

    public static AbstractNPC createNPC(LocationHolder location) {
        if (npcManager == null) {
            throw new UnsupportedOperationException("NPCManager isn't initialized yet.");
        }
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        return npcManager.createNPC0(location);
    }

    public abstract AbstractNPC createNPC0(LocationHolder location);
}
