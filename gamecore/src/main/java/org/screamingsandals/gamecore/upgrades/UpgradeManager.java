package org.screamingsandals.gamecore.upgrades;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class UpgradeManager {
    private List<Upgrade> activeUpgrades = new LinkedList<>();

    public void registerUpgrade(Upgrade upgrade) {
        activeUpgrades.add(upgrade);
    }

    public void cancelAllUpgrades() {
        activeUpgrades.forEach(Upgrade::cancel);
    }
}
