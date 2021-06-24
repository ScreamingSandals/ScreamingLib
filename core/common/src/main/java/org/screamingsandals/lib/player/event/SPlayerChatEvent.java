package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SPlayerChatEvent extends CancellableAbstractEvent {
    private final PlayerWrapper sender;
    private String message;
    private String format;
    private final List<PlayerWrapper> recipients;
}
