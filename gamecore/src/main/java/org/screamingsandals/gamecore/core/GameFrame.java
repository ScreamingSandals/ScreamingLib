package org.screamingsandals.gamecore.core;


import lombok.Data;
import org.screamingsandals.gamecore.core.cycle.GameCycle;
import org.screamingsandals.gamecore.core.data.JsonDataSaver;
import org.screamingsandals.gamecore.player.GamePlayer;
import org.screamingsandals.gamecore.resources.ResourceSpawner;
import org.screamingsandals.gamecore.store.GameStore;
import org.screamingsandals.gamecore.team.GameTeam;
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
    private int gameTime;
    private int lobbyTime;
    private int startTime;
    private List<GameTeam> gameTeams = new LinkedList<>();
    private List<GameStore> gameStores = new LinkedList<>();
    private List<ResourceSpawner> itemSpawners = new LinkedList<>();
    private GameState activeState = GameState.DISABLED;
    private GameState previousState;

    //internal shits
    private File dataFile;

    private transient int maxPlayers;
    private transient GameCycle gameCycle;
    private transient List<GamePlayer> playersInGame = new LinkedList<>();
    private transient List<GamePlayer> spectators = new LinkedList<>();

    public GameFrame(String gameName) {
        this.gameName = gameName;
        //this.dataFile = new File(Main.getInstance().getGameManager().getDataFolder(), gameName + ".json");

        loadDefaults();
    }

    public static GameFrame load(File dataFile) {
        JsonDataSaver<GameFrame> dataSaver = new JsonDataSaver<>(dataFile, GameFrame.class);
        GameFrame game = dataSaver.load();

        if (game.checkIntegrity()) {
            return game;
        }
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

    }

    public boolean checkIntegrity() {
        try {
            return arenaWorld.worldExists()
                    && lobbyWorld.worldExists()
                    && maxPlayers != 0
                    && gameTime != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadDefaults() {

    }

    public void start() {
        if (!checkIntegrity()) {
            Debug.warn("Arena " + gameName + " cannot be loaded, something is wrong with it!");
            return;
        }

        //update game events

        activeState = GameState.LOADING;
        previousState = GameState.DISABLED;

        //buildTeams();
        //countMaxPlayers();

        //gameCycle = new BungeeGameCycle(this);
        //BaseEnvironment.getTasker().runTaskRepeater(gameCycle, 0, 1, TimeUnit.SECONDS);

        //update game events
    }

    public void stop() {
        //update game events

        //gameCycle.stop();
        gameCycle = null;

        playersInGame.clear();
        spectators.clear();
        maxPlayers = 0;
        //update game events
    }
}
