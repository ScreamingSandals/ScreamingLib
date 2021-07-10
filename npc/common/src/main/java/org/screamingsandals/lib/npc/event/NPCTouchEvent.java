package org.screamingsandals.lib.npc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
public class NPCTouchEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final NPC npc;
}
