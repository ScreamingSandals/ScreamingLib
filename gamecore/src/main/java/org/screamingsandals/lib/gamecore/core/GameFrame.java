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
import org.screamingsandals.lib.gamecore.resources.ResourceManager;
import org.screamingsandals.lib.gamecore.resources.ResourceTypes;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.visuals.BossbarManager;
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
    //Game stuff
    protected String gameName;
    protected String displayedName;
    protected GameWorld gameWorld;
    protected LobbyWorld lobbyWorld;
    protected int minPlayers;
    protected int minPlayersToStart;
    protected int lobbyTime;
    protected int startTime;
    protected int gameTime;
    protected int deathmatchTime;
    protected int endTime;
    protected List<GameTeam> teams = new LinkedList<>();
    protected List<GameStore> stores = new LinkedList<>();
    protected GameState activeState = GameState.DISABLED;
    protected GameState previousState;

    //internal shits
    protected GameConfig gameConfig;
    protected UUID uuid;

    protected transient int maxPlayers;
    protected transient GameCycle gameCycle;
    protected transient List<GamePlayer> playersInGame;
    protected transient List<GamePlayer> spectators;

    //Manager
    protected ResourceManager resourceManager;
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

    public boolean checkIntegrity(boolean fireError) {
        if (!checkGameWorld(fireError)) {
            return false;
        }

        if (!checkLobbyWorld(fireError)) {
            return false;
        }

        return gameTime != 0
                && teams.size() > 0
                && stores.size() > 0;
    }

    public boolean checkGameWorld(boolean fireError) {
        if (gameWorld == null) {
            if (fireError) {
                GameCore.getErrorManager().newError(new GameError(this, ErrorType.GAME_WORLD_NOT_DEFINED, null));
            }
            return false;
        }

        if (!gameWorld.exists()) {
            final var type = ErrorType.GAME_WORLD_DOES_NOT_EXISTS;
            type.getReplaceable().put("%world%", gameWorld.getWorldAdapter().getWorldName());

            if (fireError) {
                GameCore.getErrorManager().newError(new GameError(this, type, null));
            }
            return false;
        }

        if (gameWorld.getBorder1() == null || gameWorld.getBorder2() == null) {
            if (fireError) {
                GameCore.getErrorManager().newError(new GameError(this, ErrorType.GAME_WORLD_NOT_DEFINED, null));
            }
            return false;
        }

        if (gameWorld.getSpectatorSpawn() == null) {
            if (fireError) {
                GameCore.getErrorManager().newError(new GameError(this, ErrorType.SPECTATOR_SPAWN_NOT_SET, null));
            }
            return false;
        }

        var gameWorldWorld = gameWorld.getWorldAdapter().getWorldName();
        if (!gameWorldWorld.equals(gameWorld.getBorder1().getWorld().getName())
                || !gameWorldWorld.equals(gameWorld.getBorder2().getWorld().getName())
                || !gameWorldWorld.equals(gameWorld.getSpectatorSpawn().getWorld().getName())) {
            //TODO - error manager
            Debug.warn("World of arena is different than position1, position2 or spectator spawn. Please re-do them.", true);
            return false;
        }
        return true;
    }

    public boolean checkLobbyWorld(boolean fireError) {
        if (lobbyWorld == null) {
            if (fireError) {
                GameCore.getErrorManager().newError(new GameError(this, ErrorType.LOBBY_WORLD_DOES_NOT_EXISTS, null));
            }
            return false;
        }

        if (!lobbyWorld.exists()) {
            return false;
        }

        if (lobbyWorld.getBorder1() == null || lobbyWorld.getBorder2() == null) {
            //TODO - error manager
            return false;
        }

        if (lobbyWorld.getSpawn() == null) {
            //TODO - error manager
            return false;
        }

        var lobbyWorldWorld = lobbyWorld.getWorldAdapter().getWorldName();
        if (!lobbyWorldWorld.equals(lobbyWorld.getBorder1().getWorld().getName())
                || !lobbyWorldWorld.equals(lobbyWorld.getBorder2().getWorld().getName())
                || !lobbyWorldWorld.equals(lobbyWorld.getSpawn().getWorld().getName())) {
            //TODO - error manager
            Debug.warn("Fuckup with lobby?", true);
            return false;
        }
        return true;
    }

    public void loadDefaults() {
        resourceManager.setResourceTypes(
                ResourceTypes.load(this, new File(GameCore.getPlugin().getDataFolder(), "resources.json")));
        gameConfig = new GameConfig(this);
        gameConfig.buildDefaults();
    }

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

        setGameState(GameState.LOADING);
        countMaxPlayers();
        buildTeams();

        return true;
    }

    public void startMaintenance() {

    }

    public void stopMaintenance() {

    }

    public void start() {
        if (!prepare()) {
            Debug.warn("Prepare phase failed, what is wrong?", true);
            return;
        }

        if (!checkIntegrity(true)) {
            //change to error manager
            Debug.warn("Arena " + gameName + " cannot be loaded, something is wrong with it!");
            return;
        }

        Preconditions.checkNotNull(gameCycle, "GameCycle cannot be null!")
                .runTaskRepeater(0, 1, TimeUnit.SECONDS); //We expect that dev provided valid game-cycle

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

        resourceManager.stop();
        scoreboardManager.destroy();
        bossbarManager.destroy();
        placeholderParser.destroy();

        //TODO: regen

        GameCore.getEntityManager().unregisterAll(uuid);
        GameCore.getHologramManager().destroyAll(uuid);

        setGameState(GameState.DISABLED);

        GameCore.fireEvent(new SGameDisabledEvent(this));
    }

    public void setGameState(GameState gameState) {
        previousState = activeState;
        activeState = gameState;

        GameCore.fireEvent(new SGameStateChangedEvent(this, activeState, previousState));
    }

    public boolean join(GamePlayer gamePlayer) {
        if (playersInGame.contains(gamePlayer)) {
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

        //TODO: spectators

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

    public boolean leave(GamePlayer gamePlayer) {
        final var uuid = gamePlayer.getUuid();
        if (!playersInGame.contains(gamePlayer)) {
            //TODO: mpr
            return false;
        }

        gamePlayer.setActiveGame(null);
        scoreboardManager.hideScoreboard(uuid);
        scoreboardManager.removeAll(uuid);

        gamePlayer.restore(true); //TODO: mainlobby? bungee?
        playersInGame.remove(gamePlayer);

        mpr("commands.leave.success")
                .game(this)
                .send(gamePlayer);
        GameCore.fireEvent(new SPlayerLeftGameEvent(this, gamePlayer));
        return true;
    }

    //Working with players
    public void moveAllToTeamSpawns() {
        playersInGame.forEach(gamePlayer -> gamePlayer.teleport(gamePlayer.getGameTeam().getSpawnLocation()));
    }

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

    public Set<String> getAvailableTeams() {
        final var toReturn = new HashSet<String>();

        for (var team : teams) {
            toReturn.add(team.getTeamName());
        }

        return toReturn;
    }

    //Prepare game stuff
    protected void buildTeams() {
        teams.forEach(gameTeam -> gameTeam.setActiveGame(this));
    }

    protected void countMaxPlayers() {
        for (var team : teams) {
            maxPlayers += team.getMaxPlayers();
        }
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
        return minPlayersToStart - playersInGame.size();
    }
}


