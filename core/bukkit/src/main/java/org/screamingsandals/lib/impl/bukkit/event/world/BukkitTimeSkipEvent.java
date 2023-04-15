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

package org.screamingsandals.lib.impl.bukkit.event.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.world.TimeSkipEvent;
import org.screamingsandals.lib.impl.bukkit.world.BukkitWorld;
import org.screamingsandals.lib.world.World;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitTimeSkipEvent implements TimeSkipEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.world.TimeSkipEvent event;

    // Internal cache
    private @Nullable World world;
    private @Nullable Reason reason;

    @Override
    public @NotNull World world() {
        if (world == null) {
            world = new BukkitWorld(event.getWorld());
        }
        return world;
    }

    @Override
    public @NotNull Reason reason() {
        if (reason == null) {
            reason = Reason.valueOf(event.getSkipReason().name());
        }
        return reason;
    }

    @Override
    public long skipAmount() {
        return event.getSkipAmount();
    }

    @Override
    public void skipAmount(long skipAmount) {
        event.setSkipAmount(skipAmount);
    }
}