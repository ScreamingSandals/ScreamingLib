package org.screamingsandals.lib.gamecore.team;

import lombok.Data;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class GameTeam implements Serializable {
    private LocationAdapter spawnLocation;
    private String teamName;
    private TeamColor teamColor;
    private int maxPlayers;

    //don't save this shit, not needed
    private transient boolean alive = false;
    private transient List<GamePlayer> teamPlayers = new ArrayList<>();
    private transient GameFrame activeGame;

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

    public int countPlayersInTeam() {
        return teamPlayers.size();
    }
}
