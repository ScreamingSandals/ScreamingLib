package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

@Data
public class GamePlayer {
    private final Player bukkitPlayer;
    private GameTeam gameTeam;
    private GameFrame activeGame;
    private boolean spectator;

    public void destroy() {
        activeGame.leave(this);
        gameTeam.leave(this);

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
        //game events
        spectator = false;
        teleport(gameTeam.getSpawnLocation());
    }

    public void makeSpectator() {
        //game events
        spectator = true;
        teleport(activeGame.getGameWorld().getSpectatorSpawn());
    }

    public void teleport(LocationAdapter locationAdapter) {
        PaperLib.teleportAsync(bukkitPlayer, locationAdapter.getLocation());
    }

    public void teleport(Location location) {
        PaperLib.teleportAsync(bukkitPlayer, location);
    }


    public void destroyScoreboards() {
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        scoreboardManager.deleteSavedScoreboards(this);
    }

    public Scoreboard getScoreboard(GameState gameState) {
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        return scoreboardManager.getSavedScoreboard(this, gameState.getName());
    }
}
