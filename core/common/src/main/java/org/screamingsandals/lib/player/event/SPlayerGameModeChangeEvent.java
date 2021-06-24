package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.GameMode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerGameModeChangeEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final GameMode gameMode;
}
