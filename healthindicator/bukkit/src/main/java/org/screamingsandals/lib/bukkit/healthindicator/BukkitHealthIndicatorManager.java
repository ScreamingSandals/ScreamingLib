package org.screamingsandals.lib.bukkit.healthindicator;

import org.screamingsandals.lib.healthindicator.HealthIndicator;
import org.screamingsandals.lib.healthindicator.HealthIndicatorManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.UUID;

@Service
public class BukkitHealthIndicatorManager extends HealthIndicatorManager {

    public static void init(Controllable controllable) {
        HealthIndicatorManager.init(() -> new BukkitHealthIndicatorManager(controllable));
    }

    protected BukkitHealthIndicatorManager(Controllable controllable) {
        super(controllable);
    }

    @Override
    protected HealthIndicator healthIndicator0(UUID uuid) {
        return new BukkitHealthIndicator(uuid);
    }
}
