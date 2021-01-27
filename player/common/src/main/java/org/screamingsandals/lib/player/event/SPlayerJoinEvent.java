package org.screamingsandals.lib.player.event;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerJoinEvent extends AbstractEvent {
    private final PlayerWrapper player;
}
