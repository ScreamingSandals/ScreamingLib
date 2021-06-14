package org.screamingsandals.lib.proxiedplayer.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerChatEvent extends CancellableAbstractEvent {
    private final ProxiedPlayerWrapper player;
    private final boolean isCommand;
    private String message;
    private boolean cancelled;
}
