package org.screamingsandals.lib.gamecore.team;

import lombok.Data;
import lombok.ToString;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"activeGame"})
public class GameTeam implements Serializable {
    private LocationAdapter spawnLocation;
    private String teamName;
    private TeamColor teamColor;
    private int maxPlayers;

    //don't save this shit, not needed
    private transient boolean alive = false;
    private transient List<GamePlayer> teamPlayers = new ArrayList<>();
    private transient GameFrame activeGame;

    //Internal use only.
    @Deprecated
    public GameTeam() {}

    public GameTeam(String teamName, TeamColor teamColor, int maxPlayers) {
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.maxPlayers = maxPlayers;
    }

    public void join(GamePlayer gamePlayer) {
        gamePlayer.setGameTeam(this);
        teamPlayers.add(gamePlayer);
    }

    public void leave(GamePlayer gamePlayer) {
        gamePlayer.setGameTeam(null);
        teamPlayers.remove(gamePlayer);
    }

    public void removeAllPlayers() {
        teamPlayers.forEach(gamePlayer -> gamePlayer.setGameTeam(null));
        teamPlayers.clear();
    }

    public boolean isSame(GameTeam gameTeam) {
        return gameTeam.getTeamName().equalsIgnoreCase(teamName)
                && gameTeam.getTeamColor() == teamColor;
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
