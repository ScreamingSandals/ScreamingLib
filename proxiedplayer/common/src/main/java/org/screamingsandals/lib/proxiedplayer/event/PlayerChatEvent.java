package org.screamingsandals.lib.proxiedplayer.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.screamingsandals.lib.proxiedplayer.ProxiedPlayerWrapper;
import org.screamingsandals.lib.utils.event.Cancellable;

@Data
@AllArgsConstructor
public class PlayerChatEvent implements Cancellable {
    private final ProxiedPlayerWrapper playerWrapper;
    private String message;
    private boolean cancelled;
    private final boolean isCommand;
}
