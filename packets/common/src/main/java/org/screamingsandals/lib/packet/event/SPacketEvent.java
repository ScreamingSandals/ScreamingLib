package org.screamingsandals.lib.packet.event;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractAsyncEvent;
import org.screamingsandals.lib.event.player.SPlayerEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.PacketMethod;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPacketEvent extends CancellableAbstractAsyncEvent implements SPlayerEvent {
    private final PlayerWrapper player;
    private final PacketMethod method;
    private final Object packet;

    @Override
    public PlayerWrapper getPlayer() {
        return player;
    }
}
