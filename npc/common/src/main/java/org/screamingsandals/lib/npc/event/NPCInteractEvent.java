package org.screamingsandals.lib.npc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
public class NPCInteractEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final NPC npc;
    private final InteractType interactType;

    public enum InteractType {
        LEFT_CLICK,
        RIGHT_CLICK;
    }
}
