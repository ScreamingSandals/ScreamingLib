package org.screamingsandals.lib.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.team.GameTeam;

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
