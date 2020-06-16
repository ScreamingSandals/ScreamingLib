package org.screamingsandals.lib.gamecore.core;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import org.bukkit.event.Listener;
import org.screamingsandals.lib.config.custom.ValueHolder;
import org.screamingsandals.lib.config.custom.ValueType;
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
import org.screamingsandals.lib.gamecore.events.player.game.SPlayerJoinedGameEvent;
import org.screamingsandals.lib.gamecore.events.player.game.SPlayerLeftGameEvent;
import org.screamingsandals.lib.gamecore.events.player.game.SPlayerPreJoinedGameEvent;
import org.screamingsandals.lib.gamecore.events.player.spectator.SSpectatorJoinedGameEvent;
import org.screamingsandals.lib.gamecore.events.player.spectator.SSpectatorPreJoinGameEvent;
import org.screamingsandals.lib.gamecore.placeholders.PlaceholderParser;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.resources.ResourceManager;
import org.screamingsandals.lib.gamecore.resources.ResourceTypes;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.VisualsManager;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

@Data
@EqualsAndHashCode(of = "uuid")
public abstract class GameFrame implements Serializable, Cloneable {
    //Serializable stuff that will be saved into JSON file
    protected String gameName;
    protected String displayedName;
    protected GameWorld gameWorld;
    protected LobbyWorld lobbyWorld;
    protected int minPlayers;
    protected List<GameTeam> teams = new LinkedList<>();
    protected List<GameStore> stores = new LinkedList<>();
    protected UUID uuid;
    protected ResourceManager resourceManager;

    protected Map<String, ValueHolder<?>> gameConfig = new HashMap<>();

    //Internal stuff that will not be saved and is always created at the start of the game.
    @Setter(AccessLevel.PRIVATE)
    protected transient GameState activeState = GameState.DISABLED;
    @Setter(AccessLevel.PRIVATE)
    protected transient GameState previousState;
    protected transient int maxPlayers;
    protected transient GameCycle gameCycle;
    protected transient List<GamePlayer> playersInGame;
    protected transient List<GamePlayer> spectators;

    protected transient PlaceholderParser placeholderParser;
    protected transient VisualsManager visualsManager;

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
            errorManager.newError(new GameError(this, ErrorType.CONFIG_NOT_DEFINED, null), fireError);
            gameConfig = GameCore.getGameManager().getGameConfig().getValues();
        }

        if (!checkGameWorld(fireError)) {
            return false;
        }

        if (!checkLobbyWorld(fireError)) {
            return false;
        }

        if (gameConfig.get(GameConfig.DefaultKeys.TEAMS_ENABLED).getBoolean()) {
            if (teams.size() < 2) {
                errorManager.newError(new GameError(this, ErrorType.NOT_ENOUGH_TEAMS, null), fireError);
                return false;
            }

            final var isSet = new AtomicBoolean(true);
            teams.forEach(gameTeam -> {
                if (gameTeam.getSpawn() == null) {
                    errorManager.newError(new GameError(this, ErrorType.TEAM_SPAWN_NOT_SET, null)
                            .addPlaceholder("%teamName%", gameTeam.getName()), fireError);

                    isSet.set(false);
                }
            });

            if (!isSet.get()) {
                return false;
            }
        }

        if (gameConfig.get(GameConfig.DefaultKeys.STORES_ENABLED).getBoolean() && stores.size() < 1) {
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
            errorManager.newError(new GameError(this, ErrorType.GAME_WORLD_DOES_NOT_EXISTS, null)
                    .addPlaceholder("%world%", gameWorld.getWorldAdapter().getWorldName()), fireError);

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
            errorManager.newError(new GameError(this, ErrorType.LOBBY_WORLD_DOES_NOT_EXISTS, null)
                    .addPlaceholder("%world%", lobbyWorld.getWorldAdapter().getWorldName()), fireError);
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
     * Loads game config from global config and resource types to the game.
     */
    public void loadDefaults() {
        resourceManager.setResourceTypes(
                ResourceTypes.load(this, new File(GameCore.getPlugin().getDataFolder(), "resources.json")));
        gameConfig = GameCore.getGameManager().getGameConfig().getValues();
    }

    /**
     * Prepare the whole game to start
     *
     * @return false if something fails
     */
    public boolean prepare() {
        if (displayedName == null) {
            displayedName = gameName;
        }

        updateGameConfig();

        setGameState(GameState.LOADING);
        maxPlayers = countMaxPlayers();
        buildTeams();

        return true;
    }

    /**
     * Updates shared values from default {@link GameConfig}
     * Also checks if some values are missing
     */
    private void updateGameConfig() {
        final var toUpdate = new LinkedList<ValueHolder<?>>();
        final var available = GameCore.getGameManager().getGameConfig().getValues().entrySet();
        var changed = new AtomicBoolean();

        //add new values if missing
        available.forEach(entry -> {
            if (gameConfig.containsKey(entry.getKey())) {
                return;
            }

            changed.set(true);
            gameConfig.put(entry.getKey(), entry.getValue());
        });

        if (changed.get()) {
            Debug.info("Something in config changed, saving!");
            GameCore.getGameManager().saveGame(this);
        }

        //get list of shared values
        gameConfig.values().forEach(holder -> {
            if (holder.getValueType() == ValueType.SHARED) {
                toUpdate.add(holder);
            }
        });

        //update shared values
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
            Preconditions.checkNotNull(gameCycle, "Cannot start GameCycle, it is null, contact developer!").start();
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
            Preconditions.checkNotNull(gameCycle, "Cannot stop the GameCycle, it is null!").stop();
        } catch (Exception e) {
            System.out.println("What");
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
        visualsManager.destroy();
        placeholderParser.destroy();

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

        switch (activeState) {
            case DISABLED:
            case RESTART:
                //TODO: mpr
                return false;
            case MAINTENANCE: {
                if (gamePlayer.getPlayer().hasPermission(GameCore.getInstance().getAdminPermissions())) {
                    break;
                } else {
                    //TODO: mpr
                    return false;
                }
            }
        }

        if (isGameRunning()) {
            if (gameConfig.get(GameConfig.DefaultKeys.SPECTATORS_ENABLED).getBoolean()
                    && GameCore.fireEvent(new SSpectatorPreJoinGameEvent(this, gamePlayer))) {
                gamePlayer.makeSpectator(false);

                GameCore.fireEvent(new SSpectatorJoinedGameEvent(this, gamePlayer));
                return true;
            }
            gameCycle.kickPlayer(gamePlayer);
            return false;
        }

        if (GameCore.fireEvent(new SPlayerPreJoinedGameEvent(this, gamePlayer))) {
            playersInGame.add(gamePlayer);
            gamePlayer.setActiveGame(this);
            gamePlayer.storeAndClean();
            gamePlayer.teleport(lobbyWorld.getSpawn()).then(() -> {
                visualsManager.prepareGameVisuals(gamePlayer);
                visualsManager.showGameVisuals();

                mpr("commands.join.success")
                        .game(this)
                        .send(gamePlayer);
                GameCore.fireEvent(new SPlayerJoinedGameEvent(this, gamePlayer));
            }).done();

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
        if (!playersInGame.contains(gamePlayer) && !spectators.contains(gamePlayer)) {
            //TODO: mpr
            return false;
        }

        visualsManager.hideAllForPlayer(gamePlayer);
        visualsManager.remove(gamePlayer);

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
        playersInGame.forEach(gamePlayer -> gamePlayer.teleport(gamePlayer.getGameTeam().getSpawn()).done());
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
            if (gameTeam.getName().equalsIgnoreCase(teamName)) {
                return Optional.of(gameTeam);
            }
        }
        return Optional.empty();
    }

    public boolean isTeamRegistered(String teamName) {
        for (var team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getAvailableTeamsNames() {
        final var toReturn = new HashSet<String>();

        for (var team : teams) {
            toReturn.add(team.getName());
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

    public void isEnoughPlayersInTeams() {

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

    public void switchState(GameState newState) {
        if (newState == null) {
            return;
        }

        previousState = activeState;
        activeState = newState;
    }

    public int countRemainingPlayersToStart() {
        return minPlayers - playersInGame.size();
    }

    public int getStartTime() {
        return gameConfig.get(GameConfig.DefaultKeys.START_TIME).getInt();
    }

    public int getGameTime() {
        return gameConfig.get(GameConfig.DefaultKeys.GAME_TIME).getInt();
    }

    public int getDeathmatchTime() {
        return gameConfig.get(GameConfig.DefaultKeys.DEATHMATCH_TIME).getInt();
    }

    public int getEndTime() {
        return gameConfig.get(GameConfig.DefaultKeys.END_GAME_TIME).getInt();
    }

    protected int getElapsedSeconds() {
        return gameCycle.getCurrentPhase().getElapsedTime();
    }

    public int getRawRemainingSeconds() {
        return gameCycle.getCurrentPhase().countRemainingTime();
    }

    public int getRemainingSeconds() {
        return getRawRemainingSeconds() % 60;
    }

    public int getRemainingMinutes() {
        final var minutes = getRawRemainingSeconds() / 60;
        return (int) Math.floor(minutes);
    }

    public String formatRemainingTime() {
        if (getRawRemainingSeconds() == -1) {
            return formatSecondsToString(gameConfig.get(GameConfig.DefaultKeys.GAME_TIME).getInt());
        }
        return formatSecondsToString(getRawRemainingSeconds());
    }

    public String formatSecondsToString(int rawSeconds) {
        var seconds = rawSeconds % 60;
        var finalSeconds = String.valueOf(seconds);

        if (seconds < 10) {
            finalSeconds = "0" + seconds;
        }

        final var minutes = (int) Math.floor(rawSeconds / 60.0);

        return minutes + ":" + finalSeconds;
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

    public boolean isStarting() {
        return activeState == GameState.PRE_GAME_COUNTDOWN;
    }

    /**
     * Checks if game is running in any way (in-game or not)
     *
     * @return if game is running
     */
    public boolean isRunning() {
        return isWaiting()
                || isStarting()
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

    /*
    This is responsible for loading values while deserialization.
    //TODO: test this propertly
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        playersInGame = new LinkedList<>();
        spectators = new LinkedList<>();
        placeholderParser = new PlaceholderParser(this);
        visualsManager = new VisualsManager(this);

        gameWorld.setRegenerator(GameCore.getRegenerator());
        lobbyWorld.setRegenerator(GameCore.getRegenerator());

        if (resourceManager == null) {
            resourceManager = new ResourceManager(this);
        } else {
            resourceManager.prepare(this);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


