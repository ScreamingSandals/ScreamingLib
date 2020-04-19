package org.screamingsandals.gamecore.core;


import lombok.Data;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.core.cycle.GameCycle;
import org.screamingsandals.gamecore.core.data.JsonDataSaver;
import org.screamingsandals.gamecore.player.GamePlayer;
import org.screamingsandals.gamecore.resources.ResourceSpawner;
import org.screamingsandals.gamecore.store.GameStore;
import org.screamingsandals.gamecore.team.GameTeam;
import org.screamingsandals.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.gamecore.world.GameWorld;
import org.screamingsandals.lib.debug.Debug;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Data
public abstract class GameFrame {
    //Game stuff
    private String gameName;
    private String displayedName;
    private GameWorld arenaWorld;
    private GameWorld lobbyWorld;
    private int minPlayers;
    private int minPlayersToStart;
    private int runTime;
    private int lobbyTime;
    private int startTime;
    private List<GameTeam> teams = new LinkedList<>();
    private List<GameStore> stores = new LinkedList<>();
    private List<ResourceSpawner> spawners = new LinkedList<>();
    private GameState activeState = GameState.DISABLED;
    private GameState previousState;

    //internal shits
    private File dataFile;

    private transient int maxPlayers;
    private transient GameCycle gameCycle;
    private transient List<GamePlayer> playersInGame = new LinkedList<>();
    private transient List<GamePlayer> spectators = new LinkedList<>();

    private transient ScoreboardManager scoreboardManager = new ScoreboardManager(this);

    private GameFrame(String gameName) {
        this.gameName = gameName;
        this.dataFile = new File(GameCore.getGameManager().getDataFolder(), gameName + ".json");

        loadDefaults();
    }

    public static GameFrame getGame(String gameName) {
        return null;
    }

    public void save() {
        JsonDataSaver<GameFrame> dataSaver = new JsonDataSaver<>(dataFile, GameFrame.class);
        dataSaver.save(this);
    }

    public void reload() {
        stop();

        if (!checkIntegrity()) {
            return;
        }

        scoreboardManager = new ScoreboardManager(this);
    }

    public boolean checkIntegrity() {
        return arenaWorld.worldExists()
                && lobbyWorld.worldExists()
                && maxPlayers != 0
                && runTime != 0;
    }

    public void loadDefaults() {

    }

    public void start() {
        if (!checkIntegrity()) {
            Debug.warn("Arena " + gameName + " cannot be loaded, something is wrong with it!");
            return;
        }

        //fire event- game is loading
        //This is last chance to stop loading the game

        setGameState(GameState.LOADING);

        //buildTeams();
        //countMaxPlayers();

        //gameCycle = new BungeeGameCycle(this);
        //BaseEnvironment.getTasker().runTaskRepeater(gameCycle, 0, 1, TimeUnit.SECONDS);

        //update game events
    }

    public void stop() {
        //fire event - game is stopping

        gameCycle.stop();
        if (!gameCycle.isCancelled()) {
            Debug.warn("Something is fucked up, game is not stopped!");
        }
        gameCycle = null;

        playersInGame.clear();
        spectators.clear();
        maxPlayers = 0;

        setGameState(GameState.DISABLED);
        //fire event - game stopped!
    }

    public void setGameState(GameState gameState) {
        previousState = activeState;
        activeState = gameState;
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

    public void setActiveState(GameState gameState) {
        previousState = activeState;
        activeState = gameState;
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
}
