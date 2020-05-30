package org.screamingsandals.lib.gamecore.upgrades;

import lombok.Data;
import org.screamingsandals.lib.gamecore.upgrades.base.Upgradable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class UpgradeManager {
    private Map<UUID, Upgradable> registeredUpgrades = new HashMap<>();

    public void register(Upgradable baseUpgrade) {
        registeredUpgrades.putIfAbsent(baseUpgrade.getUuid(), baseUpgrade);
    }

    public void unregister(Upgradable baseUpgrade) {
        unregister(baseUpgrade.getUuid());
    }

    public void unregister(UUID uuid) {
        registeredUpgrades.remove(uuid);
    }
}
