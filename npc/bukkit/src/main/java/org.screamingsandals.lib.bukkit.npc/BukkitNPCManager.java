package org.screamingsandals.lib.bukkit.npc;

import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.npc.NPCManager;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitNPCManager extends NPCManager {
    public static void init() {
        NPCManager.init(BukkitNPCManager::new);
    }

    @Override
    public NPC createNPC0() {
        return null;
    }
}
