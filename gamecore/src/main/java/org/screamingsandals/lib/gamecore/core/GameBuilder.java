package org.screamingsandals.lib.gamecore.core;

import org.screamingsandals.lib.gamecore.store.GameStore;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.world.GameWorld;
import org.screamingsandals.lib.gamecore.world.LobbyWorld;

import java.util.LinkedList;
import java.util.List;

public abstract class GameBuilder<T extends GameFrame> {
    private T gameFrame;
    private List<GameStore.StoreBuilder> storeBuilders = new LinkedList<>();

    public void create(String arenaName) {

    }

    public T get() {
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

    public boolean isTeamExists(String teamName) {
        for (var gameTeam : gameFrame.getTeams()) {
            if (gameTeam.getTeamName().equalsIgnoreCase(teamName)) {
                return true;
            }
        }
        return false;
    }
}
