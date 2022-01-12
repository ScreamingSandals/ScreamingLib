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

package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.event.world.ChunkLoadEvent;
import org.screamingsandals.lib.event.chunk.SChunkLoadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitChunkLoadEvent implements SChunkLoadEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final ChunkLoadEvent event;

    // Internal cache
    private ChunkHolder cachedChunk;

    @Override
    public ChunkHolder chunk() {
        if (cachedChunk == null) {
            cachedChunk = ChunkMapper.wrapChunk(event.getChunk()).orElseThrow();
        }
        return cachedChunk;
    }

    @Override
    public boolean newChunk() {
        return event.isNewChunk();
    }
}
