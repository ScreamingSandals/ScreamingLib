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

package org.screamingsandals.lib.bukkit.block;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.ext.paperlib.PaperLib;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

public final class BukkitBlock extends BasicWrapper<org.bukkit.block.Block> implements Block {
    public BukkitBlock(org.bukkit.block.@NotNull Block wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void type(@NotNull BlockTypeHolder type) {
        setType(type, false);
    }

    @Override
    public void alterTypeWithoutPhysics(@NotNull BlockTypeHolder type) {
        setType(type, true);
    }

    private void setType(@NotNull BlockTypeHolder type, boolean ignorePhysics) {
        final var bukkitLocation = wrappedObject.getLocation();
        PaperLib.getChunkAtAsync(bukkitLocation)
                .thenAccept(result -> {
                    if (!Version.isVersion(1,13)) {
                        bukkitLocation.getBlock().setType(type.as(Material.class), !ignorePhysics);
                        Reflect.getMethod(bukkitLocation.getBlock(), "setData", byte.class, boolean.class).invoke(type.legacyData(), !ignorePhysics);
                    } else {
                        bukkitLocation.getBlock().setBlockData(type.as(BlockData.class), !ignorePhysics);
                    }
                });
    }

    @Override
    public @NotNull BlockTypeHolder type() {
        if (!Version.isVersion(1,13)) {
            return BlockTypeHolder.of(wrappedObject.getState().getData());
        } else {
            return BlockTypeHolder.of(wrappedObject.getBlockData());
        }
    }

    @Override
    public @Nullable BlockSnapshot blockSnapshot() {
        return BlockSnapshots.wrapBlockState(wrappedObject.getState());
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
        if (Version.isVersion(1,13)) {
            if (type == BlockData.class) {
                return (T) wrappedObject.getBlockData();
            }
        }
        return super.as(type);
    }
}
