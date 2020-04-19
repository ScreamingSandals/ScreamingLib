package org.screamingsandals.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.screamingsandals.gamecore.config.VisualsConfig;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.GameState;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.gamecore.team.GameTeam;
import org.screamingsandals.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;
import org.screamingsandals.lib.scoreboards.scoreboard.ScoreboardCreator;

import java.util.ArrayList;

import static org.screamingsandals.lib.lang.I.m;

@Data
public class GamePlayer {
    private final Player bukkitPlayer;
    private GameTeam gameTeam;
    private GameFrame activeGame;
    private boolean spectator;

    public boolean isInGame() {
        return activeGame != null;
    }

    public void setActiveGame(GameFrame activeGame) {
        if (activeGame != null) {
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
        teleport(activeGame.getArenaWorld().getSpectatorSpawn());
    }

    public void teleport(LocationAdapter locationAdapter) {
        PaperLib.teleportAsync(bukkitPlayer, locationAdapter.getLocation());
    }

    public void teleport(Location location) {
        PaperLib.teleportAsync(bukkitPlayer, location);
    }

    public void createScoreboards() {
        //Creating scoreboards event!
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        final String scoreboardDisplayedName = m(VisualsConfig.PATH_SCOREBOARDS_NAME).get();
        Scoreboard lobbyScoreboard = ScoreboardCreator.get(GameState.WAITING.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(new ArrayList<>()));

        Scoreboard gameScoreboard = ScoreboardCreator.get(GameState.IN_GAME.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(new ArrayList<>()));

        Scoreboard deathmatchScoreboard = ScoreboardCreator.get(GameState.DEATHMATCH.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(new ArrayList<>()));

        Scoreboard end_gameScoreboard = ScoreboardCreator.get(GameState.AFTER_GAME_COUNTDOWN.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(new ArrayList<>()));

        scoreboardManager.saveScoreboard(this, lobbyScoreboard);
        scoreboardManager.saveScoreboard(this, gameScoreboard);
        scoreboardManager.saveScoreboard(this, deathmatchScoreboard);
        scoreboardManager.saveScoreboard(this, end_gameScoreboard);

        //Scoreboards created event!
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
