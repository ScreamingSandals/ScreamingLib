package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.events.player.SPlayerSwitchedToPlayer;
import org.screamingsandals.lib.gamecore.events.player.SPlayerSwitchedToSpectator;
import org.screamingsandals.lib.gamecore.events.player.SPlayerTeleportEvent;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.UUID;

@Data
@ToString(exclude = {"activeGame"})
public class GamePlayer {
    private final Player player;
    private final UUID uuid;
    private GameTeam gameTeam;
    private GameFrame activeGame;
    private boolean spectator;

    public void destroy() {
        if (gameTeam != null) {
            gameTeam.leave(this);
        }

        if (gameTeam != null) {
            gameTeam.leave(this);
        }

        destroyScoreboards();
    }

    public boolean isInGame() {
        return activeGame != null;
    }

    public void setActiveGame(GameFrame activeGame) {
        if (activeGame != null && this.activeGame != null) {
            activeGame.leave(this);
        }
        this.activeGame = activeGame;
    }

    public void makePlayer() {
        spectator = false;

        if (isInGame()) {
            teleport(gameTeam.getSpawnLocation());
        } else {
            //todo - teleport to mainlobby
        }

        GameCore.fireEvent(new SPlayerSwitchedToPlayer(this));
    }

    public void makeSpectator() {
        spectator = true;
        teleport(activeGame.getGameWorld().getSpectatorSpawn());

        GameCore.fireEvent(new SPlayerSwitchedToSpectator(this));
    }

    public void teleport(Location location) {
        GameCore.fireEvent(new SPlayerTeleportEvent(this, location, player.getLocation()));
        PaperLib.teleportAsync(player, location);
    }

    public void teleport(LocationAdapter locationAdapter) {
        teleport(locationAdapter.getLocation());
    }


    public void destroyScoreboards() {
        if (activeGame == null) {
            return;
        }
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        scoreboardManager.deleteSavedScoreboards(this);
    }

    public Scoreboard getScoreboard(GameState gameState) {
        if (activeGame == null) {
            return null;
        }
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        return scoreboardManager.getSavedScoreboard(this, gameState.getName());
    }
}
