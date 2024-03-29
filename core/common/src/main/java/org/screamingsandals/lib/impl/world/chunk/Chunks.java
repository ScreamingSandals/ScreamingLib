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

package org.screamingsandals.lib.impl.world.chunk;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.server.ChunkHolder;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.world.chunk.Chunk;

import java.util.Objects;

@ProvidedService
@ApiStatus.Internal
public abstract class Chunks {
    static {
        ChunkHolder.Provider.registerProvider(o ->
                Objects.requireNonNull(wrapChunk(o), "Cannot wrap " + o + " to Chunk")
        );
    }

    private static @Nullable Chunks chunks;

    public Chunks() {
        if (chunks != null) {
            throw new UnsupportedOperationException("Chunks is already initialized.");
        }

        chunks = this;
    }

    @Contract("null -> null")
    public static @Nullable Chunk wrapChunk(@Nullable Object chunk) {
        if (chunks == null) {
            throw new UnsupportedOperationException("Chunks is not initialized yet.");
        }
        if (chunk instanceof Chunk) {
            return (Chunk) chunk;
        }
        return chunks.wrapChunk0(chunk);
    }

    protected abstract @Nullable Chunk wrapChunk0(@Nullable Object chunk);
}
