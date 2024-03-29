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
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.event.player.PlayerEggThrowEvent;
import org.screamingsandals.lib.player.Player;

import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerEggThrowEvent implements PlayerEggThrowEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerEggThrowEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Entity egg;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull Entity eggEntity() {
        if (egg == null) {
            egg = Objects.requireNonNull(Entities.wrapEntity(event.getEgg()));
        }
        return egg;
    }

    @Override
    public boolean hatching() {
        return event.isHatching();
    }

    @Override
    public void hatching(boolean hatching) {
        event.setHatching(hatching);
    }

    @Override
    public @NotNull EntityType hatchType() {
        return EntityType.of(event.getHatchingType());
    }

    @Override
    public void hatchType(@NotNull EntityType hatchType) {
        event.setHatchingType(hatchType.as(org.bukkit.entity.EntityType.class));
    }

    @Override
    public byte hatchesNumber() {
        return event.getNumHatches();
    }

    @Override
    public void hatchesNumber(byte numHatches) {
        event.setNumHatches(numHatches);
    }
}
