package org.screamingsandals.gamecore.core;


import com.google.common.base.Preconditions;
import lombok.Data;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.config.GameConfig;
import org.screamingsandals.gamecore.core.cycle.GameCycle;
import org.screamingsandals.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.gamecore.events.core.game.SGameDisablingEvent;
import org.screamingsandals.gamecore.events.core.game.SGameLoadedEvent;
import org.screamingsandals.gamecore.events.core.game.SGameLoadingEvent;
import org.screamingsandals.gamecore.events.core.state.SGameStateChangedEvent;
import org.screamingsandals.gamecore.player.GamePlayer;
import org.screamingsandals.gamecore.resources.ResourceSpawner;
import org.screamingsandals.gamecore.resources.ResourceTypes;
import org.screamingsandals.gamecore.store.GameStore;
import org.screamingsandals.gamecore.team.GameTeam;
import org.screamingsandals.gamecore.visuals.BossbarManager;
import org.screamingsandals.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.gamecore.world.GameWorld;
import org.screamingsandals.gamecore.world.LobbyWorld;
import org.screamingsandals.lib.debug.Debug;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Data
public abstract class GameFrame {
    //Game stuff
    private String gameName;
    private String displayedName;
    private GameWorld gameWorld;
    private LobbyWorld lobbyWorld;
    private int minPlayers;
    private int minPlayersToStart;
    private int gameTime;
    private int lobbyTime;
    private int startTime;
    private int endTime;
    private int deathmatchTime;
    private List<GameTeam> teams = new LinkedList<>();
    private List<GameStore> stores = new LinkedList<>();
    private List<ResourceSpawner> spawners = new LinkedList<>();
    private ResourceTypes resourceTypes;
    private GameState activeState = GameState.DISABLED;
    private GameState previousState;

    //internal shits
    private File dataFile;
    private GameConfig gameConfig;
    private GameCycle.Type cycleType;

    private transient int maxPlayers;
    private transient GameCycle gameCycle;
    private transient List<GamePlayer> playersInGame = new LinkedList<>();
    private transient List<GamePlayer> spectators = new LinkedList<>();

    private transient ScoreboardManager scoreboardManager = new ScoreboardManager(this);
    private transient BossbarManager bossbarManager = new BossbarManager(this);

    private GameFrame(String gameName) {
        this.gameName = gameName;
        this.dataFile = new File(GameCore.getGameManager().getDataFolder(), gameName + ".json");

        loadDefaults();
    }

    public static <T> T getGame(String gameName) {
        return null;
    }

    public void reload() {
        stop();
        start();
    }

    public boolean checkIntegrity() {
        return gameWorld.exists()
                && lobbyWorld.exists()
                && maxPlayers != 0
                && gameTime != 0;
    }

    public void loadDefaults() {
        resourceTypes = ResourceTypes.load(this);
    }

    public void start() {
        if (!checkIntegrity()) {
            Debug.warn("Arena " + gameName + " cannot be loaded, something is wrong with it!");
            return;
        }

        if (GameCore.fireEvent(new SGameLoadingEvent(this))) {
            return;
        }

        setGameState(GameState.LOADING);

        buildTeams();
        countMaxPlayers();

        Preconditions.checkNotNull(gameCycle).runTaskTimer(GameCore.getPlugin(), 0, 20); //We expect that dev provided valid game-cycle

        GameCore.fireEvent(new SGameLoadedEvent(this));
    }

    public void stop() {
        if (GameCore.fireEvent(new SGameDisablingEvent(this))) {
            return;
        }

        Preconditions.checkNotNull(gameCycle).stop();

        if (!gameCycle.isCancelled()) {
            Debug.warn("Something is fucked up, game is not stopped!");
        }

        playersInGame.clear();
        spectators.clear();
        maxPlayers = 0;

        scoreboardManager.destroy();
        bossbarManager.destroy();

        setGameState(GameState.DISABLED);

        GameCore.fireEvent(new SGameDisabledEvent(this));
    }

    public void setGameState(GameState gameState) {
        previousState = activeState;
        activeState = gameState;

        GameCore.fireEvent(new SGameStateChangedEvent(this, activeState, previousState));
    }

    public void join(GamePlayer gamePlayer) {
        //update game events

        gamePlayer.setActiveGame(this);
        //gamePlayer.createScoreboards();
        //build bossbars

        gamePlayer.teleport(lobbyWorld.getSpawn());

        //update game events

        //update scoreboards and bossbars
    }

    public void leave(GamePlayer gamePlayer) {
        gamePlayer.setActiveGame(null);
        gameCycle.kickPlayer(gamePlayer);

        playersInGame.remove(gamePlayer);
    }

    //Working with players
    public void teleportPlayersToTeamSpawn() {
        for (var gamePlayer : playersInGame) {
            gamePlayer.teleport(gamePlayer.getGameTeam().getSpawnLocation());
        }
    }

    public GameTeam getTeamWithLeastPlayers() {
        GameTeam lowestTeam = null;
        for (var gameTeam : teams) {
            if (lowestTeam == null) {
                lowestTeam = gameTeam;
            }

            if (lowestTeam.countPlayersInTeam() > lowestTeam.countPlayersInTeam()) {
                lowestTeam = gameTeam;
            }
        }

        return lowestTeam;
    }

    //Prepare game stuff
    private void buildTeams() {
        for (var team : teams) {
            team.setActiveGame(this);
        }
    }

    private void countMaxPlayers() {
        for (var team : teams) {
            maxPlayers += team.getMaxPlayers();
        }
    }
}
