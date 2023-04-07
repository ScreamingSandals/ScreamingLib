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

import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.block.BukkitBlocks;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.state.BlockSnapshot;
import org.screamingsandals.lib.block.state.BlockSnapshots;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
@ServiceDependencies(dependsOn = BukkitBlocks.class)
public class BukkitBlockSnapshots extends BlockSnapshots {
    public static final boolean HAS_TILE_STATE = Reflect.has("org.bukkit.block.TileState");

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BlockSnapshot> @Nullable T wrapBlockState0(@Nullable Object blockState) {
        // ORDER IS IMPORTANT

        if (HAS_TILE_STATE) {
            if (blockState instanceof Sign) {
                return (T) new BukkitSignBlockSnapshot((Sign) blockState);
            }

            if (blockState instanceof TileState) {
                return (T) new BukkitBlockEntitySnapshot((TileState) blockState);
            }
        } else {
            if (blockState instanceof Sign) {
                return (T) new BukkitLegacySignBlockSnapshot((Sign) blockState);
            }
        }

        if (blockState instanceof BlockState) {
            return (T) new BukkitBlockSnapshot((BlockState) blockState);
        }

        return null;
    }

    @Override
    protected <T extends BlockSnapshot> @Nullable T getBlockStateFromBlock0(@Nullable Block blockHolder) {
        return blockHolder == null ? null : wrapBlockState0(blockHolder.as(org.bukkit.block.Block.class).getState());
    }
}
