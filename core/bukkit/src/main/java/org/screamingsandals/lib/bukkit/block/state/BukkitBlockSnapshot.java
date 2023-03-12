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

package org.screamingsandals.lib.bukkit.block.state;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.container.BukkitContainer;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.*;
import org.screamingsandals.lib.block.state.BlockSnapshot;

public class BukkitBlockSnapshot extends BasicWrapper<BlockState> implements BlockSnapshot {
    protected BukkitBlockSnapshot(@NotNull BlockState wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull BlockTypeHolder getType() {
        if (!Version.isVersion(1,13)) {
            return BlockTypeHolder.of(wrappedObject.getData());
        } else {
            return BlockTypeHolder.of(wrappedObject.getBlockData());
        }
    }

    @Override
    public void setType(@NotNull BlockTypeHolder type) {
        if (!Version.isVersion(1,13)) {
            wrappedObject.setType(type.as(Material.class));
            wrappedObject.setRawData(type.legacyData());
        } else {
            wrappedObject.setBlockData(type.as(BlockData.class));
        }
    }

    @Override
    public @NotNull Location getLocation() {
        return Locations.wrapLocation(wrappedObject.getLocation());
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
