package org.screamingsandals.lib.gamecore.core;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.config.GameConfig;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisablingEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameLoadedEvent;
import org.screamingsandals.lib.gamecore.events.core.state.SGameStateChangedEvent;
import org.screamingsandals.lib.gamecore.events.player.SPlayerJoinedGameEvent;
import org.screamingsandals.lib.gamecore.events.player.SPlayerLeftGameEvent;
import org.screamingsandals.lib.gamecore.events.player.SPlayerPreJoinGameEvent;
import org.screamingsandals.lib.gamecore.placeholders.PlaceholderParser;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;
import org.screamingsandals.lib.gamecore.resources.ResourceTypes;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.BossbarManager;
import org.screamingsandals.lib.gamecore.visuals.ScoreboardManager;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
public abstract class GameFrame implements Serializable {
    //Game stuff
    private String gameName;
    private String displayedName;
    private GameWorld gameWorld;
    private LobbyWorld lobbyWorld;
    private int minPlayers;
    private int minPlayersToStart;
    private int lobbyTime;
    private int startTime;
    private int gameTime;
    private int deathmatchTime;
    private int endTime;
    private List<GameTeam> teams = new LinkedList<>();
    private List<GameStore> stores = new LinkedList<>();
    private List<ResourceSpawner> spawners = new LinkedList<>();
    private ResourceTypes resourceTypes;
    private GameState activeState = GameState.DISABLED;
    private GameState previousState;

    //internal shits
    private File dataFile;
    private GameConfig gameConfig;
    private GameType gameType;
    private UUID uuid;

    private transient int maxPlayers;
    private transient GameCycle gameCycle;
    private transient List<GamePlayer> playersInGame = new LinkedList<>();
    private transient List<GamePlayer> spectators = new LinkedList<>();

    private transient PlaceholderParser placeholderParser = new PlaceholderParser(this);
    private transient ScoreboardManager scoreboardManager = new ScoreboardManager(this);
    private transient BossbarManager bossbarManager = new BossbarManager(this);

    public GameFrame(String gameName, GameType gameType) {
        this.gameName = gameName;
        this.gameType = gameType;
        this.dataFile = new File(GameCore.getGameManager().getDataFolder(), gameName + ".json");
        this.uuid = UUID.randomUUID();

        loadDefaults();
    }

    public void reload() {
        stop();
        start();
    }

    public boolean checkIntegrity() {
        if (!checkGameWorld()) {
            return false;
        }

        if (!checkLobbyWorld()) {
            return false;
        }

        return maxPlayers != 0
                && gameTime != 0
                && teams.size() > 0
                && stores.size() > 0;
    }

    public boolean checkGameWorld() {
        if (gameWorld == null) {
            GameCore.getErrorManager().newError(new GameError(this, ErrorType.GAME_WORLD_NOT_DEFINED, null));
            return false;
        }

        if (!gameWorld.exists()) {
            final var type = ErrorType.GAME_WORLD_DOES_NOT_EXISTS;
            type.getReplaceable().put("%world%", gameWorld.getWorldAdapter().getWorldName());

            GameCore.getErrorManager().newError(new GameError(this, type, null));
            return false;
        }

        if (gameWorld.getPosition1() == null || gameWorld.getPosition2() == null) {
            //TODO - error manager
            return false;
        }

        if (gameWorld.getSpectatorSpawn() == null) {
            //TODO - error manager
            return false;
        }

        var gameWorldWorld = gameWorld.getWorldAdapter().getWorldName();
        if (!gameWorldWorld.equals(gameWorld.getPosition1().getWorld().toString())
                || !gameWorldWorld.equals(gameWorld.getPosition2().getWorld().toString())
                || !gameWorldWorld.equals(gameWorld.getSpectatorSpawn().getWorld().toString())) {
            //TODO - error manager
            Debug.warn("World of arena is different than position1, position2 or spectator spawn. Please re-do them.", true);
            return false;
        }
        return true;
    }

    public boolean checkLobbyWorld() {
        if (lobbyWorld == null) {
            GameCore.getErrorManager().newError(new GameError(this, ErrorType.LOBBY_WORLD_DOES_NOT_EXISTS, null));
            return false;
        }

        if (!lobbyWorld.exists()) {
            return false;
        }

        if (lobbyWorld.getPosition1() == null || lobbyWorld.getPosition2() == null) {
            //TODO - error manager
            return false;
        }

        if (lobbyWorld.getSpawn() == null) {
            //TODO - error manager
            return false;
        }

        var lobbyWorldWorld = lobbyWorld.getWorldAdapter().getWorldName();
        if (!lobbyWorldWorld.equals(lobbyWorld.getPosition1().getWorld().toString())
                || !lobbyWorldWorld.equals(lobbyWorld.getPosition2().getWorld().toString())
                || !lobbyWorldWorld.equals(lobbyWorld.getSpawn().getWorld().toString())) {
            //TODO - error manager
            Debug.warn("World of arena is different than position1, position2 or spectator spawn. Please re-do them.", true);
            return false;
        }
        return true;
    }

    public void loadDefaults() {
        resourceTypes = ResourceTypes.load(this, new File(GameCore.getPlugin().getDataFolder(), "resources.json"));
        gameConfig = new GameConfig(this);
        gameConfig.buildDefaults();
    }

    public void prepare() {
        setGameState(GameState.LOADING);
        buildTeams();
        countMaxPlayers();
    }

    public void start() {
        if (!checkIntegrity()) {
            //change to error manager
            Debug.warn("Arena " + gameName + " cannot be loaded, something is wrong with it!");
            return;
        }

        prepare();

        Preconditions.checkNotNull(gameCycle, "GameCycle cannot be null!")
                .runTaskRepeater(0, 1, TimeUnit.SECONDS); //We expect that dev provided valid game-cycle

        gameType = gameCycle.getGameType();

        GameCore.fireEvent(new SGameLoadedEvent(this));
    }

    public void stop() {
        if (activeState == GameState.DISABLED) {
            return;
        }

        if (!GameCore.fireEvent(new SGameDisablingEvent(this))) {
            return;
        }

        Preconditions.checkNotNull(gameCycle, "GameCycle cannot be null!").stop();

        if (!gameCycle.hasStopped()) {
            //change to error manager
            Debug.warn("Something is fucked up, game is not stopped!", true);

            //last try
            gameCycle.stop();
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
        if (GameCore.fireEvent(new SPlayerPreJoinGameEvent(this, gamePlayer))) {
            gamePlayer.setActiveGame(this);
            gamePlayer.teleport(lobbyWorld.getSpawn());

            //update scoreboards and bossbars
            GameCore.fireEvent(new SPlayerJoinedGameEvent(this, gamePlayer));
        }
    }

    public void leave(GamePlayer gamePlayer) {
        gamePlayer.setActiveGame(null);
        gameCycle.kickPlayer(gamePlayer);

        playersInGame.remove(gamePlayer);

        GameCore.fireEvent(new SPlayerLeftGameEvent(this, gamePlayer));
    }

    //Working with players
    public void teleportPlayersToTeamSpawn() {
        for (var gamePlayer : playersInGame) {
            gamePlayer.teleport(gamePlayer.getGameTeam().getSpawnLocation());
        }
    }

    public Optional<GameTeam> getTeamWithLeastPlayers() {
        GameTeam lowestTeam = null;
        for (var gameTeam : teams) {
            if (lowestTeam == null) {
                lowestTeam = gameTeam;
            }

            if (lowestTeam.countPlayersInTeam() > lowestTeam.countPlayersInTeam()) {
                lowestTeam = gameTeam;
            }
        }

        return lowestTeam != null ? Optional.of(lowestTeam) : Optional.empty();
    }

    public Optional<GameTeam> getRegisteredTeam(String teamName) {
        for (var gameTeam : teams) {
           if (gameTeam.getTeamName().equalsIgnoreCase(teamName)) {
               return Optional.of(gameTeam);
           }
        }

        return Optional.empty();
    }

    public boolean isTeamRegistered(String teamName) {
        for (var team : teams) {
            if (team.getTeamName().equalsIgnoreCase(teamName)) {
                return true;
            }
        }
        return false;
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

