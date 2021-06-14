package org.screamingsandals.lib.proxiedplayer.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.CancellableAbstractAsyncEvent;
import org.screamingsandals.lib.proxiedplayer.PendingConnection;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerLoginEvent extends CancellableAbstractAsyncEvent {
    private final PendingConnection player;
    private Result result = Result.ALLOW;
    private Component cancelMessage = Component.text("Nope.");
}
