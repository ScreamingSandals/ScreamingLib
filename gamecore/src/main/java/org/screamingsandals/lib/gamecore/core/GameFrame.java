package org.screamingsandals.lib.gamecore.core;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.config.GameConfig;
import org.screamingsandals.lib.gamecore.core.config.GameValue;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycle;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisablingEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameLoadedEvent;
import org.screamingsandals.lib.gamecore.events.core.state.SGameStateChangedEvent;
import org.screamingsandals.lib.gamecore.events.player.*;
import org.screamingsandals.lib.gamecore.placeholders.PlaceholderParser;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.resources.ResourceManager;
import org.screamingsandals.lib.gamecore.resources.ResourceTypes;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.bossbars.BossbarManager;
import org.screamingsandals.lib.gamecore.visuals.scoreboards.GameScoreboard;
import org.screamingsandals.lib.gamecore.visuals.scoreboards.ScoreboardManager;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

@Data
public abstract class GameFrame implements Serializable {
    //Serializable stuff that will be saved into JSON file
    protected String gameName;
    protected String displayedName;
    protected GameWorld gameWorld;
    protected LobbyWorld lobbyWorld;
    protected int minPlayers;
    protected List<GameTeam> teams = new LinkedList<>();
    protected List<GameStore> stores = new LinkedList<>();
    protected GameState activeState = GameState.DISABLED;
    protected GameState previousState;
    protected UUID uuid;
    protected ResourceManager resourceManager;

    protected Map<String, GameConfig.ValueHolder<?>> gameConfig = new HashMap<>();

    //Internal stuff that will not be saved and is always created at the start of the game.
    protected transient int maxPlayers;
    protected transient GameCycle gameCycle;
    protected transient List<GamePlayer> playersInGame;
    protected transient List<GamePlayer> spectators;

    protected transient PlaceholderParser placeholderParser;
    protected transient ScoreboardManager scoreboardManager;
    protected transient BossbarManager bossbarManager;

    public GameFrame(String gameName) {
        this.gameName = gameName;
        this.uuid = UUID.randomUUID();

        resourceManager = new ResourceManager(this);
        loadDefaults();
    }

    public void reload() {
        stop();
        start();
    }

    /**
     * Checks integrity of the game
     *
     * @param fireError if this method should fire errors to players or console
     * @return true if game is alright
     */
    public boolean checkIntegrity(boolean fireError) {
        final var errorManager = GameCore.getErrorManager();

        if (gameConfig.isEmpty()) {
            errorManager.newError(new GameError(this, ErrorType.GAME_CONFIG_NOT_DEFINED, null), fireError);
            gameConfig = GameCore.getGameManager().getGameConfig().getGameValues();
        }

        if (!checkGameWorld(fireError)) {
            return false;
        }

        if (!checkLobbyWorld(fireError)) {
            return false;
        }

        if (gameConfig.get(GameConfig.DefaultKeys.TEAMS_ENABLED).getBooleanValue() && teams.size() < 2) {
            errorManager.newError(new GameError(this, ErrorType.NOT_ENOUGH_TEAMS, null), fireError);
            return false;
        }

        if (gameConfig.get(GameConfig.DefaultKeys.STORES_ENABLED).getBooleanValue() && stores.size() < 1) {
            errorManager.newError(new GameError(this, ErrorType.NOT_ENOUGH_STORES, null), fireError);
            return false;
        }
        return true;
    }

    /**
     * Checks if the game world is alright
     *
     * @param fireError if this method should fire errors to players or console
     * @return true if world is ok
     */
    public boolean checkGameWorld(boolean fireError) {
        final var errorManager = GameCore.getErrorManager();

        if (gameWorld == null) {
            errorManager.newError(new GameError(this, ErrorType.GAME_WORLD_NOT_DEFINED, null), fireError);
            return false;
        }

        if (!gameWorld.exists()) {
            final var type = ErrorType.GAME_WORLD_DOES_NOT_EXISTS;

            type.getReplaceable().put("%world%", gameWorld.getWorldAdapter().getWorldName());
            errorManager.newError(new GameError(this, type, null), fireError);

            return false;
        }

        if (gameWorld.getBorder1() == null || gameWorld.getBorder2() == null) {
            errorManager.newError(new GameError(this, ErrorType.GAME_WORLD_NOT_DEFINED, null), fireError);
            return false;
        }

        if (gameWorld.getSpectatorSpawn() == null) {
            errorManager.newError(new GameError(this, ErrorType.SPECTATOR_SPAWN_NOT_SET, null), fireError);
            return false;
        }

        var gameWorldWorld = gameWorld.getWorldAdapter().getWorldName();
        if (!gameWorldWorld.equals(gameWorld.getBorder1().getWorld().getName())
                || !gameWorldWorld.equals(gameWorld.getBorder2().getWorld().getName())) {
            errorManager.newError(new GameError(this, ErrorType.GAME_WORLD_BAD_BORDER, null), fireError);
            return false;
        }
        return true;
    }

    /**
     * Checks if the lobby world is alright
     *
     * @param fireError if this method should fire errors to players or console
     * @return true if lobby is ok
     */
    public boolean checkLobbyWorld(boolean fireError) {
        final var errorManager = GameCore.getErrorManager();

        if (lobbyWorld == null) {
            errorManager.newError(new GameError(this, ErrorType.LOBBY_WORLD_NOT_DEFINED, null), fireError);
            return false;
        }

        if (!lobbyWorld.exists()) {
            final var type = ErrorType.LOBBY_WORLD_DOES_NOT_EXISTS;

            type.getReplaceable().put("%world%", gameWorld.getWorldAdapter().getWorldName());
            errorManager.newError(new GameError(this, type, null), fireError);
            return false;
        }

        if (lobbyWorld.getBorder1() == null || lobbyWorld.getBorder2() == null) {
            errorManager.newError(new GameError(this, ErrorType.LOBBY_WORLD_NOT_DEFINED, null), fireError);
            return false;
        }

        if (lobbyWorld.getSpawn() == null) {
            errorManager.newError(new GameError(this, ErrorType.LOBBY_SPAWN_NOT_SET, null), fireError);
            return false;
        }

        var lobbyWorldWorld = lobbyWorld.getWorldAdapter().getWorldName();
        if (!lobbyWorldWorld.equals(lobbyWorld.getBorder1().getWorld().getName())
                || !lobbyWorldWorld.equals(lobbyWorld.getBorder2().getWorld().getName())) {
            errorManager.newError(new GameError(this, ErrorType.LOBBY_WORLD_BAD_BORDER, null), fireError);
            return false;
        }
        return true;
    }

    /**
     * Loads default values for the game
     * TODO: load resource manager always? Hell nah
     */
    public void loadDefaults() {
        resourceManager.setResourceTypes(
                ResourceTypes.load(this, new File(GameCore.getPlugin().getDataFolder(), "resources.json")));
        gameConfig = GameCore.getGameManager().getGameConfig().getGameValues();
    }

    /**
     * Prepare the whole game to start
     *
     * @return false if something fails
     */
    public boolean prepare() {
        playersInGame = new LinkedList<>();
        spectators = new LinkedList<>();
        placeholderParser = new PlaceholderParser(this);
        scoreboardManager = new ScoreboardManager(this);
        bossbarManager = new BossbarManager(this);

        if (resourceManager == null) {
            resourceManager = new ResourceManager(this);
        } else {
            resourceManager.prepare(this);
        }

        updateGameConfig();

        setGameState(GameState.LOADING);
        maxPlayers = countMaxPlayers();
        buildTeams();

        return true;
    }

    /**
     * Load {@link GameValue} SHARED values from default config
     */
    private void updateGameConfig() {
        final var toUpdate = new LinkedList<GameConfig.ValueHolder<?>>();
        gameConfig.values().forEach(holder -> {
            if (holder.getGameValue() == GameValue.SHARED) {
                toUpdate.add(holder);
            }
        });

        toUpdate.forEach(holder -> {
            final var key = holder.getKey();
            gameConfig.put(key, GameCore.getGameManager().getGameConfig().getValueHolder(key));
        });
    }

    //TODO
    public void startMaintenance() {

    }

    //TODO
    public void stopMaintenance() {

    }

    /**
     * Starts the game
     */
    public void start() {
        final var errorManager = GameCore.getErrorManager();

        if (!prepare() || !checkIntegrity(true)) {
            errorManager.newError(new GameError(this, ErrorType.PREPARE_FAILED, null), true);
            return;
        }

        try {
            Preconditions.checkNotNull(gameCycle, "GameCycle cannot be null!")
                    .runTaskRepeater(0, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            errorManager.newError(new GameError(this, ErrorType.UNKNOWN, e), true);
        }

        GameCore.fireEvent(new SGameLoadedEvent(this));
    }

    /**
     * Stops the game
     */
    public void stop() {
        final var errorManager = GameCore.getErrorManager();

        if (activeState == GameState.DISABLED) {
            return;
        }

        if (!GameCore.fireEvent(new SGameDisablingEvent(this))) {
            return;
        }

        try {
            Preconditions.checkNotNull(gameCycle, "GameCycle cannot be null!").stop();
        } catch (Exception e) {
            errorManager.newError(new GameError(this, ErrorType.UNKNOWN, e), true);
            return;
        }

        if (!gameCycle.hasStopped()) {
            Debug.warn("Something is fucked up, game is not stopped!", true);
            //last try
            gameCycle.stop();
        }

        playersInGame.clear();
        spectators.clear();
        maxPlayers = 0;

        resourceManager.stop();
        scoreboardManager.destroy();
        placeholderParser.destroy();

        //TODO: regen

        GameCore.getEntityManager().unregisterAll(uuid);
        GameCore.getHologramManager().destroyAll(uuid);

        setGameState(GameState.DISABLED);

        GameCore.fireEvent(new SGameDisabledEvent(this));
    }

    /**
     * Sets {@link GameState} activeState to new one
     * previousState = activeState
     *
     * @param gameState state to set
     */
    public void setGameState(GameState gameState) {
        previousState = activeState;
        activeState = gameState;

        GameCore.fireEvent(new SGameStateChangedEvent(this, activeState, previousState));
    }

    /**
     * Joins {@link GamePlayer} to this game
     *
     * @param gamePlayer player to join
     * @return true if success
     */
    public boolean join(GamePlayer gamePlayer) {
        if (playersInGame.contains(gamePlayer)) {
            //TODO: mpr
            return false;
        }

        if (activeState == GameState.DISABLED) {
            //TODO: mpr
            return false;
        }

        if (activeState == GameState.RESTART) {
            //TODO: mpr
            return false;
        }

        if (activeState == GameState.MAINTENANCE
                && !gamePlayer.getPlayer().hasPermission(GameCore.getInstance().getAdminPermissions())) {
            //TODO: mpr
            return false;
        }

        if (isGameRunning()) {
            if (gameConfig.get(GameConfig.DefaultKeys.SPECTATORS_ENABLED).getBooleanValue()
                    && GameCore.fireEvent(new SSpectatorPreJoinGameEvent(this, gamePlayer))) {
                gamePlayer.makeSpectator(false);
                GameCore.fireEvent(new SSpectatorJoinedGameEvent(this, gamePlayer));
                return true;
            }
            gameCycle.kickPlayer(gamePlayer);
            return false;
        }

        if (GameCore.fireEvent(new SPlayerPreJoinGameEvent(this, gamePlayer))) {
            playersInGame.add(gamePlayer);
            gamePlayer.setActiveGame(this);
            gamePlayer.storeAndClean();
            gamePlayer.teleport(lobbyWorld.getSpawn());
            createScoreboards(gamePlayer);
            createBossbars(gamePlayer);

            scoreboardManager.show(gamePlayer, activeState);
            mpr("commands.join.success")
                    .game(this)
                    .send(gamePlayer);
            GameCore.fireEvent(new SPlayerJoinedGameEvent(this, gamePlayer));
            return true;
        }

        return false;
    }

    /**
     * Leaves {@link GamePlayer} to this game
     *
     * @param gamePlayer player to leave
     * @return true if success
     */
    public boolean leave(GamePlayer gamePlayer) {
        final var uuid = gamePlayer.getUuid();
        if (!playersInGame.contains(gamePlayer) || !spectators.contains(gamePlayer)) {
            //TODO: mpr
            return false;
        }

        scoreboardManager.hideScoreboard(uuid);
        scoreboardManager.removeAll(uuid);


        gamePlayer.setActiveGame(null);
        gamePlayer.setSpectator(false);

        gamePlayer.restore(true); //TODO: mainlobby? bungee?
        playersInGame.remove(gamePlayer);

        mpr("commands.leave.success")
                .game(this)
                .send(gamePlayer);
        GameCore.fireEvent(new SPlayerLeftGameEvent(this, gamePlayer));
        return true;
    }

    public void moveAllToTeamSpawns() {
        playersInGame.forEach(gamePlayer -> gamePlayer.teleport(gamePlayer.getGameTeam().getSpawnLocation()));
    }

    //Working with teams
    public GameTeam getTeamWithLeastPlayers() {
        final TreeMap<Integer, GameTeam> playersInTeams = new TreeMap<>();
        teams.forEach(gameTeam -> playersInTeams.put(gameTeam.getTeamPlayers().size(), gameTeam));

        return playersInTeams.firstEntry().getValue();
    }

    public GameTeam getTeamWithMostPlayers() {
        final TreeMap<Integer, GameTeam> playersInTeams = new TreeMap<>();
        teams.forEach(gameTeam -> playersInTeams.put(gameTeam.getTeamPlayers().size(), gameTeam));

        return playersInTeams.lastEntry().getValue();
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

    public Set<String> getAvailableTeamsNames() {
        final var toReturn = new HashSet<String>();

        for (var team : teams) {
            toReturn.add(team.getTeamName());
        }

        return toReturn;
    }

    public void sortPlayersToTeams() {
        playersInGame.forEach(gamePlayer -> {
            if (gamePlayer.getGameTeam() == null) {
                getTeamWithLeastPlayers().join(gamePlayer);
            }
        });
    }

    //Prepare game stuff
    protected void buildTeams() {
        teams.forEach(gameTeam -> gameTeam.setActiveGame(this));
    }

    protected int countMaxPlayers() {
        int toReturn = 0;

        if (teams.isEmpty()) {
            return toReturn;
        }

        for (var team : teams) {
            toReturn += team.getMaxPlayers();
        }

        return toReturn;
    }

    protected void createScoreboards(GamePlayer gamePlayer) {
        scoreboardManager.save(gamePlayer, GameScoreboard.ContentBuilder.get(gamePlayer, GameState.WAITING, this));
        scoreboardManager.save(gamePlayer, GameScoreboard.ContentBuilder.get(gamePlayer, GameState.PRE_GAME_COUNTDOWN, this));
        scoreboardManager.save(gamePlayer, GameScoreboard.ContentBuilder.get(gamePlayer, GameState.IN_GAME, this));
        scoreboardManager.save(gamePlayer, GameScoreboard.ContentBuilder.get(gamePlayer, GameState.DEATHMATCH, this));
        scoreboardManager.save(gamePlayer, GameScoreboard.ContentBuilder.get(gamePlayer, GameState.AFTER_GAME_COUNTDOWN, this));
    }

    protected void createBossbars(GamePlayer gamePlayer) {
    }

    public void updateScoreboards() {
        playersInGame.forEach(gamePlayer -> {
            final var uuid = gamePlayer.getUuid();

            if (scoreboardManager.getActiveScoreboards().containsKey(uuid)) {
                final var gameScoreboard = scoreboardManager.getActiveScoreboards().get(uuid);

                if (gameScoreboard.getGameState() == activeState) {
                    gameScoreboard.update(this);
                    return;
                }

                scoreboardManager.hideScoreboard(uuid);
            }

            final var scoreboard = scoreboardManager.getSavedScoreboard(uuid, activeState.getName());

            if (scoreboard.isEmpty()) {
                return;
            }

            final var gameScoreboard = scoreboard.get();
            gameScoreboard.update(this);

            scoreboardManager.show(gamePlayer, gameScoreboard);
        });
    }

    public int countRemainingPlayersToStart() {
        return minPlayers - playersInGame.size();
    }

    public int getStartTime() {
        return gameConfig.get(GameConfig.DefaultKeys.START_TIME).getIntValue();
    }

    public int getGameTime() {
        return gameConfig.get(GameConfig.DefaultKeys.GAME_TIME).getIntValue();
    }

    public int getDeathmatchTime() {
        return gameConfig.get(GameConfig.DefaultKeys.DEATHMATCH_TIME).getIntValue();
    }

    public int getEndTime() {
        return gameConfig.get(GameConfig.DefaultKeys.END_GAME_TIME).getIntValue();
    }

    public boolean isFull() {
        //TODO: in case of some fuckup (>) remove after testing?
        return maxPlayers >= playersInGame.size();
    }

    public boolean isEmpty() {
        return playersInGame.size() == 0;
    }

    public boolean isWaiting() {
        return activeState == GameState.WAITING;
    }

    /**
     * Checks if game is running in any way (in-game or not)
     *
     * @return if game is running
     */
    public boolean isRunning() {
        return isWaiting()
                || activeState == GameState.PRE_GAME_COUNTDOWN
                || activeState == GameState.IN_GAME
                || activeState == GameState.DEATHMATCH
                || activeState == GameState.AFTER_GAME_COUNTDOWN
                || activeState == GameState.CUSTOM
                || activeState == GameState.LOADING;
    }

    /**
     * Checks if game is in-game (playing)
     *
     * @return if game is running
     */
    public boolean isGameRunning() {
        return activeState == GameState.IN_GAME
                || activeState == GameState.DEATHMATCH
                || activeState == GameState.AFTER_GAME_COUNTDOWN
                || activeState == GameState.CUSTOM;
    }
}


