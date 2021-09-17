package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerPacketEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final PlayerWrapper player;
    private final PacketMethod method;
    private Object packet;

    @Override
    public PlayerWrapper getPlayer() {
        return player;
    }
}
