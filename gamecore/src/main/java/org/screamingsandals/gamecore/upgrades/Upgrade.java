package org.screamingsandals.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.gamecore.player.GamePlayer;
import org.screamingsandals.gamecore.team.GameTeam;

@Data
public abstract class Upgrade {
    private GamePlayer gamePlayer;
    private GameTeam gameTeam;
    private String upgradeName;

    public Upgrade(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void cancel() {

    }
}
