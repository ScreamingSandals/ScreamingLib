package org.screamingsandals.lib.healthindicator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.visuals.DatableVisual;

public interface HealthIndicator extends DatableVisual<HealthIndicator> {
    static HealthIndicator of() {
        return HealthIndicatorManager.healthIndicator();
    }

    HealthIndicator showHealthInTabList(boolean flag);

    HealthIndicator symbol(Component symbol);

    HealthIndicator symbol(ComponentLike symbol);

    HealthIndicator startUpdateTask(long time, TaskerTime unit);

    HealthIndicator addTrackedPlayer(PlayerWrapper player);

    HealthIndicator removeTrackedPlayer(PlayerWrapper player);

    default HealthIndicator title(Component component) {
        return symbol(component);
    }

    default HealthIndicator title(ComponentLike component) {
        return symbol(component);
    }
}
