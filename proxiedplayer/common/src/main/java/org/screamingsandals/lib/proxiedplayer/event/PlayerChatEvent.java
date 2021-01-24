package org.screamingsandals.lib.proxiedplayer.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class PlayerChatEvent extends CancellableAbstractEvent {
    private final ProxiedPlayerWrapper playerWrapper;
    private String message;
    private boolean cancelled;
    private final boolean isCommand;
}
