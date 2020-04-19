package org.screamingsandals.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.gamecore.resources.ResourceSpawner;

import java.io.Serializable;

@Data
public class Upgrade implements Cloneable, Serializable {
    private ResourceSpawner resourceSpawner; //the item spawner we are affecting with this upgrade
    private int changeSpeed; //how much of spawn speed are we changing
    private int duration; //how long will this upgrade be present
    private int maxSpawned; //max spawned resources from this upgrade
    private String upgradeName;
}
