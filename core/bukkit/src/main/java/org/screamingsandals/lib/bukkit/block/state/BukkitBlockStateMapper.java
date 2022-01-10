/*
 * Copyright 2022 ScreamingSandals
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

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.screamingsandals.lib.bukkit.block.BukkitBlockMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;

import java.util.Optional;

@Service(dependsOn = {
        BukkitBlockMapper.class
})
public class BukkitBlockStateMapper extends BlockStateMapper {

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BlockStateHolder> Optional<T> wrapBlockState0(Object blockState) {
        // ORDER IS IMPORTANT

        if (blockState instanceof Sign) {
            return Optional.of((T) new SignBlockStateHolder((Sign) blockState));
        }

        if (blockState instanceof TileState) {
            return Optional.of((T) new TileBlockStateHolder((TileState) blockState));
        }

        if (blockState instanceof BlockState) {
            return Optional.of((T) new GenericBlockStateHolder((BlockState) blockState));
        }

        return Optional.empty();
    }

    @Override
    protected <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock0(BlockHolder blockHolder) {
        return wrapBlockState0(blockHolder.as(Block.class).getState());
    }
}
