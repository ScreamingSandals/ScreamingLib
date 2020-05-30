package org.screamingsandals.lib.gamecore.upgrades;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.resources.ResourceSpawner;
import org.screamingsandals.lib.gamecore.upgrades.base.BaseUpgrade;
import org.screamingsandals.lib.gamecore.upgrades.base.UpgradeType;

@EqualsAndHashCode(callSuper = false)
public class SpawnerUpgrade extends BaseUpgrade {
    private final ResourceSpawner resourceSpawner;
    private boolean isActive;

    public SpawnerUpgrade(ResourceSpawner resourceSpawner, UpgradeType upgradeType, int duration) {
        super(upgradeType, duration);
        this.resourceSpawner = resourceSpawner;
    }

    @Override
    public UpgradeType getType() {
        return UpgradeType.SPAWNER;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
}
