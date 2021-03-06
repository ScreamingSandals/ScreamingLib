package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerKickEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private Component leaveMessage;
    private Component kickReason;
}
