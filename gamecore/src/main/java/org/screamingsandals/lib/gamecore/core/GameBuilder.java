package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.world.BaseWorld;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.util.LinkedList;
import java.util.List;

import static org.screamingsandals.lib.lang.I.mpr;

@Data
public abstract class GameBuilder<T extends GameFrame> {
    private T gameFrame;
    private List<GameStore> gameStores = new LinkedList<>();

    public boolean create(String arenaName, GameType gameType, Player player) {
        if (GameCore.getGameManager().isGameRegistered(arenaName)) {
            mpr("core.errors.game-already-created").send(player);
            return false;
        }
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
        gameFrame.getStores().forEach(GameStore::kill);
    }

    public boolean isCreated() {
        return gameFrame != null;
    }

    public boolean isReadyToSave() {
        return gameFrame.checkIntegrity();
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
