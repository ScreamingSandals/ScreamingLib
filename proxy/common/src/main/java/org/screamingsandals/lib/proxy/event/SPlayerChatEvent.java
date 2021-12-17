package org.screamingsandals.lib.proxy.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.proxy.ProxiedPlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerChatEvent implements SCancellableEvent {
    private final ProxiedPlayerWrapper player;
    private final boolean isCommand;
    private String message;
    private boolean cancelled;
}
