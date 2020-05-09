package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.adapter.WorldAdapter;
import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.world.BaseWorld;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.util.LinkedList;
import java.util.List;

@Data
public abstract class GameBuilder<T extends GameFrame> {
    private T gameFrame;
    private List<GameStore.StoreBuilder> storeBuilders = new LinkedList<>();
    private List<GameStore> gameStores = new LinkedList<>();

    public void create(String arenaName) {

    }

    public T get(Player player) {
        return gameFrame;
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
            gameWorld = new GameWorld();
        }

        gameWorld.setWorldAdapter(new WorldAdapter(worldName));
    }

    public void setGameWorldPosition(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var gameWorld = gameFrame.getGameWorld();

        if (gameWorld == null) {
            gameWorld = new GameWorld();
            gameFrame.setGameWorld(gameWorld);
        }

        setWorldPosition(adapter, gameWorld, whichOne);
    }

    public void setLobbyWorldPosition(Location location, int whichOne) {
        final var adapter = new LocationAdapter(location);
        var lobbyWorld = gameFrame.getLobbyWorld();

        if (lobbyWorld == null) {
            lobbyWorld = new LobbyWorld();
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
