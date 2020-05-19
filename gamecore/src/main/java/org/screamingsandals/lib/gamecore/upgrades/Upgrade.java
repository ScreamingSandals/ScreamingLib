package org.screamingsandals.lib.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@Data
public abstract class Upgrade {
    private GamePlayer gamePlayer;
    private String upgradeName;

    public Upgrade(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void cancel() {

    }
}
