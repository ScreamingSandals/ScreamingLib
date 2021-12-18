package org.screamingsandals.lib.packet.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.SCancellableAsyncEvent;
import org.screamingsandals.lib.event.player.SPlayerEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPacketEvent implements SPlayerEvent, SCancellableAsyncEvent {
    private final PlayerWrapper player;
    private final PacketMethod method;
    private final Object packet;
    private boolean cancelled;

    @Override
    public PlayerWrapper getPlayer() {
        return player;
    }
}
