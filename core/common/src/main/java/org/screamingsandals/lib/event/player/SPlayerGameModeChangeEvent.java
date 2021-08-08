package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerGameModeChangeEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final GameModeHolder gameMode;
}
