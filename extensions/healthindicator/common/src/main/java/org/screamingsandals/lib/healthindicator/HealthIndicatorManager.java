/*
 * Copyright 2024 ScreamingSandals
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.event.player.PlayerLeaveEvent;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.visuals.Visual;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@ServiceDependencies(dependsOn = {
        Core.class,
        PacketMapper.class
})
public class HealthIndicatorManager {
    private static @Nullable HealthIndicatorManager manager;
    protected final @NotNull Map<@NotNull UUID, HealthIndicator> activeIndicators = new HashMap<>();

    @ApiStatus.Internal
    public HealthIndicatorManager() {
        if (manager != null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is already initialized!");
        }
        manager = this;
    }

    public static @NotNull Map<@NotNull UUID, @NotNull HealthIndicator> getActiveIndicators() {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }
        return Map.copyOf(manager.activeIndicators);
    }

    @Contract("null -> null")
    public static @Nullable HealthIndicator getHealthIndicator(@Nullable UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }
        if (uuid == null) {
            return null;
        }
        return manager.activeIndicators.get(uuid);
    }

    public static void addHealthIndicator(@NotNull HealthIndicator healthIndicator) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }
        manager.activeIndicators.put(healthIndicator.uuid(), healthIndicator);
    }

    public static void removeHealthIndicator(@Nullable UUID uuid) {
        var hi = getHealthIndicator(uuid);
        if (hi != null) {
            removeHealthIndicator(hi);
        }
    }

    public static void removeHealthIndicator(@NotNull HealthIndicator healthIndicator) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }
        manager.activeIndicators.remove(healthIndicator.uuid());
    }

    public static @NotNull HealthIndicator healthIndicator() {
        return healthIndicator(UUID.randomUUID());
    }

    public static @NotNull HealthIndicator healthIndicator(@NotNull UUID uuid) {
        if (manager == null) {
            throw new UnsupportedOperationException("HealthIndicatorManager is not initialized yet!");
        }

        final var healthIndicator = new HealthIndicatorImpl(uuid);
        addHealthIndicator(healthIndicator);
        return healthIndicator;
    }

    @OnPreDisable
    public void destroy() {
        getActiveIndicators()
                .values()
                .forEach(Visual::destroy);
        activeIndicators.clear();
    }

    @OnEvent
    public void onLeave(@NotNull PlayerLeaveEvent event) {
        if (activeIndicators.isEmpty()) {
            return;
        }

        getActiveIndicators().forEach((key, indicator) -> {
            if (indicator.viewers().contains(event.player())) {
                indicator.removeViewer(event.player());
                indicator.removeTrackedPlayer(event.player());
            }
            if (!indicator.hasViewers()) {
                removeHealthIndicator(indicator);
            }
        });
    }
}
