package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;
import org.screamingsandals.lib.gamecore.resources.SpawnerHologramHandler;
import org.screamingsandals.lib.gamecore.resources.editor.SpawnerEditor;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.utils.GameUtils;
import org.screamingsandals.lib.gamecore.visuals.holograms.HologramType;
import org.screamingsandals.lib.gamecore.world.BaseWorld;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;
import org.screamingsandals.lib.lang.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;
import static org.screamingsandals.lib.lang.I.mpr;

@Data
public abstract class GameBuilder<T extends GameFrame> {
    private T gameFrame;
    private String gameName;
    private List<GameStore> gameStores = new LinkedList<>();
    private SpawnerEditor spawnerEditor;

    public boolean create(String gameName, GameType gameType, Player player) {
        if (GameCore.getGameManager().isGameRegistered(gameName)) {
            mpr("core.errors.game-already-created").send(player);
            return false;
        }

        this.gameName = gameName;
        return true;
    }

    public void load(T gameFrame, Player player) {
        if (gameFrame == null) {
            mpr("core.errors.game-does-not-exists").send(player);
        }
        this.gameFrame = gameFrame;

        final var gameStores = gameFrame.getStores();
        if (gameStores == null) {
            return;
        }

        this.gameStores.addAll(gameStores);
    }

    public void save(Player player) {
        gameFrame.getStores().forEach(GameStore::remove);
    }

    public boolean isCreated() {
        return gameFrame != null;
    }

    public boolean isReadyToSave(boolean fireError) {
        return gameFrame.checkIntegrity(fireError);
    }

    public void setDisplayName(String displayName) {
        gameFrame.setDisplayedName(displayName);
    }

    public void setGameWorld(GameWorld gameWorld) {
        gameFrame.setGameWorld(gameWorld);
    }

    public void setLobbyWorld(LobbyWorld lobbyWorld) {
        gameFrame.setLobbyWorld(lobbyWorld);
    }

    public void setMinPlayers(int minPlayers) {
        gameFrame.setMinPlayers(minPlayers);
    }

    public void setMinPlayersToStart(int minPlayers) {
        gameFrame.setMinPlayersToStart(minPlayers);
    }

    public void setGameTime(int gameTime) {
        gameFrame.setGameTime(gameTime);
    }

    public void setLobbyTime(int lobbyTime) {
        gameFrame.setLobbyTime(lobbyTime);
    }

    public void setStartTime(int startTime) {
        gameFrame.setStartTime(startTime);
    }

    public void setEndTime(int endTime) {
        gameFrame.setEndTime(endTime);
    }

    public void setDeathmatchTime(int deathmatchTime) {
        gameFrame.setDeathmatchTime(deathmatchTime);
    }

    public void addTeam(GameTeam gameTeam) {
        gameFrame.getTeams().add(gameTeam);
    }

    public void addSpawner(ResourceSpawner resourceSpawner, Player player) {
        gameFrame.getResourceManager().register(resourceSpawner);

        buildHologram(resourceSpawner, gameFrame, player);
    }

    public void addStore(GameStore gameStore) {
        gameFrame.getStores().add(gameStore);
    }

    public void removeTeam(String teamName) {
        gameFrame.getTeams().removeIf(gameTeam -> gameTeam.getTeamName().equalsIgnoreCase(teamName));
    }

    public void removeTeam(GameTeam gameTeam) {
        gameFrame.getTeams().removeIf(gameTeam1 -> gameTeam1.isSame(gameTeam));
    }

    public void setGameWorld(String worldName) {
        var gameWorld = gameFrame.getGameWorld();
        if (gameWorld == null) {
            gameWorld = new GameWorld(worldName);
        }

        gameWorld.setWorldAdapter(new WorldAdapter(worldName));
    }

    public void setGameWorldPosition(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var gameWorld = gameFrame.getGameWorld();

        if (gameWorld == null) {
            gameWorld = new GameWorld(location.getWorld().getName());
            gameFrame.setGameWorld(gameWorld);
        }

        setWorldPosition(adapter, gameWorld, whichOne);
    }

    public void setLobbyWorldPosition(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var lobbyWorld = gameFrame.getLobbyWorld();

        if (lobbyWorld == null) {
            lobbyWorld = new LobbyWorld(location.getWorld().getName());
            gameFrame.setLobbyWorld(lobbyWorld);
        }

        setWorldPosition(adapter, lobbyWorld, whichOne);
    }

    public void buildHologram(ResourceSpawner spawner, GameFrame currentGame, Player player) {
        final List<String> lines = new ArrayList<>();
        var period = spawner.getPeriod();
        final var timeUnit = spawner.getTimeUnit();
        final var team = spawner.getGameTeam();
        final var maxSpawned = spawner.getMaxSpawned();

        if (timeUnit == TimeUnit.MILLISECONDS) {
            period = GameUtils.convertMilisecondsToTick(period);
        }

        lines.add(Utils.colorize("&a&lGameBuilder"));
        lines.addAll(m("game-builder.spawners.hologram")
                .replace("%color%", spawner.getType().getChatColor())
                .replace("%type%", spawner.getType().getName())
                .replace("%mat%", spawner.getType().getMaterial())
                .replace("%spawnAmount%", spawner.getAmount())
                .replace("%time-unit%", period + " " + GameUtils.convertTimeUnitToLanguage(period, timeUnit))
                .replace("%team%", team == null ? GameUtils.convertNullToLanguage() : team.getTeamName())
                .replace("%amount%", maxSpawned == -1 ? GameUtils.getInfinityLanguage() : maxSpawned)
                .replace("%booleanValue%", spawner.isHologram())
                .getList());

        final var gameHologram = GameCore.getHologramManager()
                .spawnTouchableHologram(currentGame, HologramType.BUILDER_SPAWNER, player, spawner.getLocation().getLocation(), lines);
        gameHologram.setUuid(spawner.getUuid());
        gameHologram.setHandler(new SpawnerHologramHandler());
    }

    private void setWorldPosition(LocationAdapter adapter, BaseWorld baseWorld, int whichOne) {
        if (baseWorld.getWorldAdapter() == null) {
            baseWorld.setWorldAdapter(new WorldAdapter(adapter.getWorld().toString()));
        }

        switch (whichOne) {
            case 1: {
                baseWorld.setPosition1(adapter);
                break;
            }
            case 2: {
                baseWorld.setPosition2(adapter);
                break;
            }
            default: {
                Debug.info("Not possible. Report to developers on GitHub!", true);
            }
        }
    }

    public boolean isTeamExists(String teamName) {
        for (var gameTeam : gameFrame.getTeams()) {
            if (gameTeam.getTeamName().equalsIgnoreCase(teamName)) {
                return true;
            }
        }
        return false;
    }

}
