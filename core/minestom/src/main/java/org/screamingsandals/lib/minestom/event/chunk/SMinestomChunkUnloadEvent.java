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

package org.screamingsandals.lib.minestom.event.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SMinestomChunkUnloadEvent implements SChunkUnloadEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final InstanceChunkUnloadEvent event;

    // Internal cache
    private ChunkHolder cachedChunk;
    private boolean cancelled = false;

    @Override
    public ChunkHolder chunk() {
        if (cachedChunk == null) {
            cachedChunk = ChunkMapper.wrapChunk(event.getChunk()).orElseThrow();
        }
        return cachedChunk;
    }

    @Override
    public boolean saveChunk() {
        return true;
    }

    @Override
    public void saveChunk(boolean saveChunk) {
        // empty stub
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        if (cancel && !cancelled) {
            event.getInstance().loadChunk(event.getChunkX(), event.getChunkZ());
        } else if (!cancel && cancelled) {
            event.getInstance().unloadChunk(event.getChunk());
        }
        cancelled = cancel;
    }
}
