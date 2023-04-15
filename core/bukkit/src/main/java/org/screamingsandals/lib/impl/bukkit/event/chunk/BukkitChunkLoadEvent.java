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

package org.screamingsandals.lib.impl.bukkit.event.chunk;

import lombok.*;
import lombok.experimental.Accessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.world.chunk.BukkitChunk;
import org.screamingsandals.lib.event.chunk.ChunkLoadEvent;
import org.screamingsandals.lib.world.chunk.Chunk;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitChunkLoadEvent implements ChunkLoadEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.world.ChunkLoadEvent event;

    // Internal cache
    private @Nullable Chunk cachedChunk;

    @Override
    public @NotNull Chunk chunk() {
        if (cachedChunk == null) {
            cachedChunk = new BukkitChunk(event.getChunk());
        }
        return cachedChunk;
    }

    @Override
    public boolean newChunk() {
        return event.isNewChunk();
    }
}