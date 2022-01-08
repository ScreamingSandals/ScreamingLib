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

package org.screamingsandals.lib.block.state;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class BlockStateMapper {

    private static BlockStateMapper blockStateMapper;

    @ApiStatus.Internal
    public BlockStateMapper() {
        if (blockStateMapper != null) {
            throw new UnsupportedOperationException("BlockStateMapper is already initialized.");
        }

        blockStateMapper = this;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockStateHolder> Optional<T> wrapBlockState(Object blockState) {
        if (blockStateMapper == null) {
            throw new UnsupportedOperationException("BlockStateMapper is not initialized yet.");
        }
        if (blockState instanceof BlockStateHolder) {
            return Optional.of((T) blockState);
        }
        return blockStateMapper.wrapBlockState0(blockState);
    }

    protected abstract <T extends BlockStateHolder> Optional<T> wrapBlockState0(Object blockState);

    public static <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock(BlockHolder blockHolder) {
        if (blockStateMapper == null) {
            throw new UnsupportedOperationException("BlockStateMapper is not initialized yet.");
        }
        return blockStateMapper.getBlockStateFromBlock0(blockHolder);
    }

    protected abstract <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock0(BlockHolder blockHolder);
}
