package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.screamingsandals.lib.gamecore.config.VisualsConfig;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;
import org.screamingsandals.lib.scoreboards.scoreboard.ScoreboardCreator;

import static org.screamingsandals.lib.lang.I.m;

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
        teleport(activeGame.getGameWorld().getSpectatorSpawn());
    }

    public void teleport(LocationAdapter locationAdapter) {
        PaperLib.teleportAsync(bukkitPlayer, locationAdapter.getLocation());
    }

    public void teleport(Location location) {
        PaperLib.teleportAsync(bukkitPlayer, location);
    }

    public void createScoreboards() {
        //Creating scoreboards event!
        //move this to the minigame itself, not core?
        final ScoreboardManager scoreboardManager = activeGame.getScoreboardManager();
        final String scoreboardDisplayedName = m(VisualsConfig.PATH_SCOREBOARDS_NAME).get();
        Scoreboard lobbyScoreboard = ScoreboardCreator.get(GameState.WAITING.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(m(VisualsConfig.PATH_SCOREBOARDS_CONTENT_LOBBY).getList()));

        Scoreboard gameScoreboard = ScoreboardCreator.get(GameState.IN_GAME.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(m(VisualsConfig.PATH_SCOREBOARDS_CONTENT_GAME).getList()));

        Scoreboard deathmatchScoreboard = ScoreboardCreator.get(GameState.DEATHMATCH.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(m(VisualsConfig.PATH_SCOREBOARDS_CONTENT_DEATHMATCH).getList()));

        Scoreboard end_gameScoreboard = ScoreboardCreator.get(GameState.AFTER_GAME_COUNTDOWN.getName())
                .create(scoreboardDisplayedName, DisplaySlot.SIDEBAR, Scoreboard.sortLines(m(VisualsConfig.PATH_SCOREBOARDS_CONTENT_END_GAME).getList()));

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
