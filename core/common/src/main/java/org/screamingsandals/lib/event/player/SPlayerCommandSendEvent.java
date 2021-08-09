package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerCommandSendEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final Collection<String> commands;
}
