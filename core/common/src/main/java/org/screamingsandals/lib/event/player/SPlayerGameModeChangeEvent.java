package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerGameModeChangeEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<GameModeHolder> gameMode;

    public SPlayerGameModeChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                      ImmutableObjectLink<GameModeHolder> gameMode) {
        super(player);
        this.gameMode = gameMode;
    }

    public GameModeHolder getGameMode() {
        return gameMode.get();
    }
}
