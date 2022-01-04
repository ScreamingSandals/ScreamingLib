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

package org.screamingsandals.lib.world.chunk;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class ChunkMapper {

    private static ChunkMapper chunkMapper;

    @ApiStatus.Internal
    public ChunkMapper() {
        if (chunkMapper != null) {
            throw new UnsupportedOperationException("ChunkMapper is already initialized.");
        }

        chunkMapper = this;
    }

    public static Optional<ChunkHolder> wrapChunk(Object chunk) {
        if (chunkMapper == null) {
            throw new UnsupportedOperationException("ChunkMapper is not initialized yet.");
        }
        if (chunk instanceof ChunkHolder) {
            return Optional.of((ChunkHolder) chunk);
        }
        return chunkMapper.wrapChunk0(chunk);
    }

    protected abstract Optional<ChunkHolder> wrapChunk0(Object chunk);
}
