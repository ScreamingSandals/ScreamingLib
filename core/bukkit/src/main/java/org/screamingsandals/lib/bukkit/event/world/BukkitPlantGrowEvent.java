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

package org.screamingsandals.lib.bukkit.event.world;

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.world.PlantGrowEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.collections.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPlantGrowEvent implements PlantGrowEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull StructureGrowEvent event;

    // Internal cache
    private @Nullable Collection<@NotNull BlockSnapshot> collection;
    private @Nullable Location location;
    private @Nullable Player player;
    private boolean playerCached;

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> blockStates() {
        if (collection == null) {
            collection = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockSnapshots.<BlockSnapshot>wrapBlockState(o).orElseThrow());
        }
        return collection;
    }

    @Override
    public @NotNull Location getLocation() {
        if (location == null) {
            location = Locations.wrapLocation(event.getLocation());
        }
        return location;
    }

    @Override
    public @Nullable Player player() {
        if (!playerCached) {
            if (event.getPlayer() != null) {
                player = new BukkitPlayer(event.getPlayer());
            }
            playerCached = true;
        }
        return player;
    }

    @Override
    public boolean boneMealed() {
        return event.isFromBonemeal();
    }
}
