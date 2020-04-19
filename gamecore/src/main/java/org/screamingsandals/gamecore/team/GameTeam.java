package org.screamingsandals.gamecore.team;

import lombok.Data;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.gamecore.player.GamePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class GameTeam implements Serializable {
    private String teamName;
    private TeamColor teamColor;
    private int maxPlayers;
    private LocationAdapter spawnLocation;

    //don't save this shit, not needed
    private transient boolean alive = false;
    private transient List<GamePlayer> teamPlayers = new ArrayList<>();
    private transient GameFrame activeGame;

    public void join(GamePlayer gamePlayer) {
        gamePlayer.setGameTeam(this);
    }

    public void leave(GamePlayer gamePlayer) {
        gamePlayer.setGameTeam(null);
    }

    public int countPlayersInTeam() {
        return teamPlayers.size();
    }
}
