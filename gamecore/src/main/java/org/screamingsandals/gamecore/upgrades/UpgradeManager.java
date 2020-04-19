package org.screamingsandals.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.gamecore.resources.ResourceSpawner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UpgradeManager {
    private Map<ResourceSpawner, List<Upgrade>> activeUpgrades = new HashMap<>();

    public void registerUpgrade(Upgrade upgrade) {
        final ResourceSpawner resourceSpawner = upgrade.getResourceSpawner();
        if (activeUpgrades.containsKey(resourceSpawner)) {
            activeUpgrades.get(resourceSpawner).add(upgrade);
        } else {
            activeUpgrades.put(resourceSpawner, List.of(upgrade));
        }
    }

    public List<Upgrade> getActiveUpgrades(ResourceSpawner resourceSpawner) {
        final var upgrades = activeUpgrades.get(resourceSpawner);
        if (upgrades == null) {
            return List.of();
        }

        return upgrades;
    }
}
