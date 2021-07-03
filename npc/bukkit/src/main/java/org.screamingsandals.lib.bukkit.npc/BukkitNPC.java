package org.screamingsandals.lib.bukkit.npc;

import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.List;

public class BukkitNPC extends NPC {

    @Override
    public boolean isVisibleToPlayer(PlayerWrapper player) {
        return false;
    }

    @Override
    protected void setDisplayName0(List<TextEntry> name) {

    }
}
