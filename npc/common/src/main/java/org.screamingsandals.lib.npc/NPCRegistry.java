package org.screamingsandals.lib.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NPCRegistry {
    private static final List<NPC> npcs = new ArrayList<>();

    public static List<NPC> getRegisteredNPCS() {
        return List.copyOf(npcs);
    }

    public static void registerNPC(NPC npc) {
        if (npc == null) {
            throw new UnsupportedOperationException("NPC cannot be null!");
        }
        if (npcs.contains(npc)) {
            throw new UnsupportedOperationException("NPC: " + npc.getUUID().toString() + " has already been registered!");
        }
        npcs.add(npc);
    }

    public static void unregisterNPC(NPC npc) {
        if (npc == null) {
            throw new UnsupportedOperationException("NPC cannot be null!");
        }
        if (!npcs.contains(npc)) {
            throw new UnsupportedOperationException("NPC has not been registered yet!");
        }
        npcs.remove(npc);
        npc.destroy();
    }

    public static Optional<NPC> getNPCByUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        return npcs.stream()
                .filter(npc -> npc.getUUID().equals(uuid))
                .findAny();
    }
}
