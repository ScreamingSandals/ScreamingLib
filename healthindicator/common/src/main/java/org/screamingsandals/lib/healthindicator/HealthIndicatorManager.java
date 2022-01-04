/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.healthindicator;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service(dependsOn = {
        Core.class,
        PacketMapper.class
})
public class HealthIndicatorManager {
    private static HealthIndicatorManager manager;
    protected final Map<UUID, HealthIndicator> activeIndicators = new HashMap<>();

    @ApiStatus.Internal
    public HealthIndicatorManager() {
        if (manager != null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is already initialized!");
        }
        manager = this;
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

    protected HealthIndicator healthIndicator0(UUID uuid) {
        return new HealthIndicatorImpl(uuid);
    }

    @OnPreDisable
    public void destroy() {
        getActiveIndicators()
                .values()
                .forEach(Visual::destroy);
        manager.activeIndicators.clear();
    }

    @OnEvent
    public void onLeave(SPlayerLeaveEvent event) {
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
