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

package org.screamingsandals.lib.impl.bukkit.event.block;

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitItemEntity;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.ItemEntity;
import org.screamingsandals.lib.event.block.BlockDropItemEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;

import java.util.Collection;
import java.util.Objects;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitBlockDropItemEvent implements BlockDropItemEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.BlockDropItemEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable BlockSnapshot blockState;
    private @Nullable Collection<@NotNull ItemEntity> items;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull BlockSnapshot state() {
        if (blockState == null) {
            blockState = Objects.requireNonNull(BlockSnapshots.wrapBlockSnapshot(event.getBlockState()));
        }
        return blockState;
    }

    @Override
    public @NotNull Collection<@NotNull ItemEntity> items() {
        if (items == null) {
            items = new CollectionLinkedToCollection<>(event.getItems(), entityItem -> entityItem.as(Item.class), BukkitItemEntity::new);
        }
        return items;
    }
}
