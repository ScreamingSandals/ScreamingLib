package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.AbstractService;

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

    public static NPC createNPC() {
        if (npcManager == null) {
            throw new UnsupportedOperationException("NPCManager isn't initialized yet.");
        }
        return npcManager.createNPC0();
    }

    public abstract NPC createNPC0();
}
