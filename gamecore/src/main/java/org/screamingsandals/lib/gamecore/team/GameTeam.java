package org.screamingsandals.lib.gamecore.team;

import lombok.Data;
import lombok.ToString;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.player.teams.SPlayerLeftTeamEvent;
import org.screamingsandals.lib.gamecore.events.player.teams.SPlayerPreJoinedTeamEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@ToString(exclude = {"activeGame"})
public class GameTeam implements Serializable {
    private LocationAdapter spawn;
    private String name;
    private TeamColor color;
    private int maxPlayers;

    //don't save this shit, not needed
    private transient boolean alive = false;
    private transient List<GamePlayer> teamPlayers;
    private transient GameFrame activeGame;

    public GameTeam(String name, TeamColor color, int maxPlayers) {
        this.name = name;
        this.color = color;
        this.maxPlayers = maxPlayers;
    }

    public void setActiveGame(GameFrame gameFrame) {
        teamPlayers = new LinkedList<>();

        if (gameFrame == null) {
            alive = false;
            activeGame = null;
            return;
        }

        alive = true;
        activeGame = gameFrame;
    }

    public boolean join(GamePlayer gamePlayer) {
        if (!GameCore.fireEvent(new SPlayerPreJoinedTeamEvent(activeGame, gamePlayer, this))) {
            //TODO: mpr
            return false;
        }

        if (activeGame == null || gamePlayer == null) {
            //TODO: mpr
            return false;
        }

        if (gamePlayer.getGameTeam() != null) {
            gamePlayer.leaveTeam();
        }

        if (isFull()) {
            //TODO: mpr
            return false;
        }

        gamePlayer.setGameTeam(this);
        teamPlayers.add(gamePlayer);

        GameCore.fireEvent(new SPlayerPreJoinedTeamEvent(activeGame, gamePlayer, this));
        return true;
    }

    public void leave(GamePlayer gamePlayer) {
        gamePlayer.setGameTeam(null);
        teamPlayers.remove(gamePlayer);

        GameCore.fireEvent(new SPlayerLeftTeamEvent(activeGame, gamePlayer, this));
    }

    public void removeAllPlayers() {
        teamPlayers.forEach(this::leave);
        teamPlayers.clear();
    }

    public boolean isSame(GameTeam gameTeam) {
        return gameTeam.getName().equalsIgnoreCase(name)
                && gameTeam.getColor() == color;
    }

    public int countPlayersInTeam() {
        return teamPlayers.size();
    }

    public boolean isFull() {
        return countPlayersInTeam() == maxPlayers;
    }

    public boolean isEmpty() {
        return countPlayersInTeam() == 0;
    }
}
