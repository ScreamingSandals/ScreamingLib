package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerGameModeChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;

@Data
public class SBukkitPlayerGameModeChangeEvent implements SPlayerGameModeChangeEvent, BukkitCancellable {
    private final PlayerGameModeChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private GameModeHolder gameMode;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public GameModeHolder getGameMode() {
        if (gameMode == null) {
            gameMode = GameModeHolder.of(event.getNewGameMode());
        }
        return gameMode;
    }
}
