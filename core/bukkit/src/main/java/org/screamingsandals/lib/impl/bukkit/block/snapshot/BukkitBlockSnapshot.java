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

package org.screamingsandals.lib.impl.bukkit.block.snapshot;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.block.BlockUtils1_8;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlock1_8;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.container.BukkitContainer;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.impl.world.Locations;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.*;
import org.screamingsandals.lib.block.snapshot.BlockSnapshot;

public class BukkitBlockSnapshot extends BasicWrapper<BlockState> implements BlockSnapshot {
    protected BukkitBlockSnapshot(@NotNull BlockState wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Block block() {
        if (!BukkitFeature.FLATTENING.isSupported()) {
            return BlockUtils1_8.getBlock(wrappedObject);
        } else {
            return Block.of(wrappedObject.getBlockData());
        }
    }

    @Override
    public void block(@NotNull Block type) {
        if (!BukkitFeature.FLATTENING.isSupported()) {
            wrappedObject.setType(type.as(Material.class));
            if (type instanceof BukkitBlock1_8) {
                wrappedObject.setRawData(((BukkitBlock1_8) type).legacyData());
                BlockUtils1_8.finishSettingBlock(wrappedObject, (BukkitBlock1_8) type, false);
            }
        } else {
            wrappedObject.setBlockData(type.as(BlockData.class));
        }
    }

    @Override
    public @Nullable Location location() {
        if (wrappedObject.isPlaced()) {
            return Locations.wrapLocation(wrappedObject.getLocation());
        }
        return null;
    }

    @Override
    public @Nullable BlockPlacement blockPlacement() {
        if (wrappedObject.isPlaced()) {
            return new BukkitBlockPlacement(wrappedObject.getBlock());
        }
        return null;
    }

    @Override
    public boolean updateBlock(boolean force, boolean applyPhysics) {
        return wrappedObject.update(force, applyPhysics);
    }

    @Override
    public boolean holdsInventory() {
        return wrappedObject instanceof InventoryHolder;
    }

    @Override
    public @Nullable Container getInventory() {
        if (wrappedObject instanceof InventoryHolder) {
            return new BukkitContainer(((InventoryHolder) wrappedObject).getInventory());
        }
        return null;
    }
}
