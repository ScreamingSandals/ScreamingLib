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

import lombok.*;
import lombok.experimental.Accessors;

import lombok.experimental.ExtensionMethod;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.world.SpongeAbsorbEvent;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitSpongeAbsorbEvent implements SpongeAbsorbEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.block.SpongeAbsorbEvent event;

    // Internal cache
    private @Nullable BlockPlacement block;
    private @Nullable Collection<@NotNull BlockSnapshot> waterBlocks;

    @Override
    public @NotNull BlockPlacement block() {
        if (block == null) {
            block = new BukkitBlockPlacement(event.getBlock());
        }
        return block;
    }

    @Override
    public @NotNull Collection<@NotNull BlockSnapshot> waterBlocks() {
        if (waterBlocks == null) {
            waterBlocks = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockSnapshots.<BlockSnapshot>wrapBlockSnapshot(o).orElseThrow());
        }
        return waterBlocks;
    }
}
