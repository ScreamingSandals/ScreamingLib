package org.screamingsandals.lib.healthindicator;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
@ServiceDependencies(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        Tasker.class
})
public abstract class HealthIndicatorManager {
    private static HealthIndicatorManager manager;
    protected final Map<UUID, HealthIndicator> activeIndicators = new HashMap<>();

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Supplier<HealthIndicatorManager> supplier) {
        if (manager != null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is already initialized");
        }
        manager = supplier.get();
    }

    protected HealthIndicatorManager(Controllable controllable) {
        controllable.disable(this::destroy).postEnable(() ->
            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave)
        );
    }

    public static Map<UUID, HealthIndicator> getActiveIndicators() {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        return Map.copyOf(manager.activeIndicators);
    }

    public static Optional<HealthIndicator> getHealthIndicator(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        return Optional.ofNullable(manager.activeIndicators.get(uuid));
    }

    public static void addHealthIndicator(HealthIndicator healthIndicator) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        manager.activeIndicators.put(healthIndicator.getUuid(), healthIndicator);
    }

    public static void removeHealthIndicator(UUID uuid) {
        getHealthIndicator(uuid).ifPresent(HealthIndicatorManager::removeHealthIndicator);
    }

    public static void removeHealthIndicator(HealthIndicator healthIndicator) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        manager.activeIndicators.remove(healthIndicator.getUuid());
    }

    public static HealthIndicator healthIndicator() {
        return healthIndicator(UUID.randomUUID());
    }

    public static HealthIndicator healthIndicator(UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        final var healthIndicator = manager.healthIndicator0(uuid);
        addHealthIndicator(healthIndicator);
        return healthIndicator;
    }

    protected abstract HealthIndicator healthIndicator0(UUID uuid);

    protected void destroy() {
        Map.copyOf(getActiveIndicators())
                .values()
                .forEach(Visual::destroy);
        manager.activeIndicators.clear();
    }

    private void onLeave(SPlayerLeaveEvent event) {
        if (activeIndicators.isEmpty()) {
            return;
        }

        getActiveIndicators().forEach((key, indicator) -> {
            if (indicator.getViewers().contains(event.getPlayer())) {
                indicator.removeViewer(event.getPlayer());
                indicator.removeTrackedPlayer(event.getPlayer());
            }
            if (!indicator.hasViewers()) {
                removeHealthIndicator(indicator);
            }
        });
    }
}
