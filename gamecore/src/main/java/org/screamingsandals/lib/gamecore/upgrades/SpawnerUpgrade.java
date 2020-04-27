package org.screamingsandals.lib.gamecore.upgrades;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;

@EqualsAndHashCode(callSuper = false)
@Data
public class SpawnerUpgrade extends Upgrade {
    private ResourceSpawner resourceSpawner; //the item spawner we are affecting with this upgrade
    private int changeSpeed; //how much of spawn speed are we changing
    private int duration; //how long will this upgrade be present
    private int maxSpawned; //max spawned resources from this upgrade

    public SpawnerUpgrade(GamePlayer gamePlayer, ResourceSpawner resourceSpawner) {
        super(gamePlayer);
        this.resourceSpawner = resourceSpawner;
    }
}
