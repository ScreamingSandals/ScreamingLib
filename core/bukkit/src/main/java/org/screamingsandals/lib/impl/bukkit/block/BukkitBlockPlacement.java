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

package org.screamingsandals.lib.impl.bukkit.block;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;
import org.screamingsandals.lib.impl.block.snapshot.BlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.impl.ext.paperlib.PaperLib;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;

public final class BukkitBlockPlacement extends BasicWrapper<org.bukkit.block.Block> implements BlockPlacement {
    public BukkitBlockPlacement(org.bukkit.block.@NotNull Block wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void block(@NotNull Block type) {
        setType(type, false);
    }

    @Override
    public void alterBlockWithoutPhysics(@NotNull Block type) {
        setType(type, true);
    }

    private void setType(@NotNull Block type, boolean ignorePhysics) {
        final var bukkitLocation = wrappedObject.getLocation();
        PaperLib.getChunkAtAsync(bukkitLocation)
                .thenAccept(result -> {
                    if (!BukkitFeature.FLATTENING_MATERIAL.isSupported()) {
                        bukkitLocation.getBlock().setType(type.as(Material.class), !ignorePhysics);
                        Reflect.getMethod(bukkitLocation.getBlock(), "setData", byte.class, boolean.class).invoke(type instanceof BukkitBlock1_8 ? ((BukkitBlock1_8) type).legacyData() : 0, !ignorePhysics);
                    } else {
                        bukkitLocation.getBlock().setBlockData(type.as(BlockData.class), !ignorePhysics);
                    }
                });
    }

    @Override
    public @NotNull Block block() {
        if (!BukkitFeature.FLATTENING_MATERIAL.isSupported()) {
            return Block.of(wrappedObject.getState().getData());
        } else {
            return Block.of(wrappedObject.getBlockData());
        }
    }

    @Override
    public @Nullable BlockSnapshot blockSnapshot() {
        return BlockSnapshots.wrapBlockSnapshot(wrappedObject.getState());
    }

    @Override
    public void breakNaturally() {
        wrappedObject.breakNaturally();
    }

    @Override
    public @NotNull Location location() {
        return Locations.wrapLocation(wrappedObject.getLocation());
    }

    @Override
    public boolean isEmpty() {
        return wrappedObject.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (BukkitFeature.FLATTENING_MATERIAL.isSupported()) {
            if (type == BlockData.class) {
                return (T) wrappedObject.getBlockData();
            }
        }
        return super.as(type);
    }
}
