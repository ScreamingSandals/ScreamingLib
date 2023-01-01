/*
 * Copyright 2023 ScreamingSandals
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
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

    @Contract("_ -> this")
    default @NotNull HealthIndicator title(@NotNull Component component) {
        return symbol(component);
    }

    @Contract("_ -> this")
    default @NotNull HealthIndicator title(@NotNull ComponentLike component) {
        return symbol(component);
    }
}
