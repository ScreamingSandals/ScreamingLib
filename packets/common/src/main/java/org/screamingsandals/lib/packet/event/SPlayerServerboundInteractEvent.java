package org.screamingsandals.lib.packet.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractAsyncEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
public class SPlayerServerboundInteractEvent extends CancellableAbstractAsyncEvent {
    private final PlayerWrapper player;
    private final int entityId;
    private final InteractType interactType;
}
