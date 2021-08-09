package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SPlayerCommandPreprocessEvent extends CancellableAbstractEvent {
    private PlayerWrapper player;
    private String command;
}
