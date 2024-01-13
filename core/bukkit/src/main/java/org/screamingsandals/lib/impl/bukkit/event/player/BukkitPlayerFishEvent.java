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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.event.player.PlayerFishEvent;
import org.screamingsandals.lib.player.Player;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerFishEvent implements PlayerFishEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerFishEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Entity entity;
    private boolean entityCached;
    private @Nullable State state;
    private @Nullable Entity hookEntity;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @Nullable Entity caughtEntity() {
        if (!entityCached) {
            if (event.getCaught() != null) {
                entity = Objects.requireNonNull(Entities.wrapEntity(event.getCaught()));
            }
            entityCached = true;
        }
        return entity;
    }

    @Override
    public int exp() {
        return event.getExpToDrop();
    }

    @Override
    public void exp(int exp) {
        event.setExpToDrop(exp);
    }

    @Override
    public @NotNull State state() {
        if (state == null) {
            state = State.convert(event.getState().name());
        }
        return state;
    }

    @Override
    public @NotNull Entity hookEntity() {
        if (hookEntity == null) {
            hookEntity = Objects.requireNonNull(Entities.wrapEntity(event.getHook()));
        }
        return hookEntity;
    }
}
