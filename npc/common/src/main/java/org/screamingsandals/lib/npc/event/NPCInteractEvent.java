package org.screamingsandals.lib.npc.event;

import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.event.VisualsTouchEvent;

/**
 * An event signifying that a NPC has been interacted with.
 */
public class NPCInteractEvent extends VisualsTouchEvent<NPC> {
    public NPCInteractEvent(PlayerWrapper player, NPC visual, InteractType interactType) {
        super(player, visual, interactType);
    }
}
