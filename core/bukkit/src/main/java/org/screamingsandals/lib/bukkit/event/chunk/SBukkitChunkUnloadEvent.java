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

import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.event.Cancellable;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitChunkUnloadEvent implements SChunkUnloadEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final ChunkUnloadEvent event;

    // Internal cache
    private ChunkHolder cachedChunk;

    @Override
    public ChunkHolder chunk() {
        if (cachedChunk == null) {
            cachedChunk = new BukkitChunkHolder(event.getChunk());
        }
        return cachedChunk;
    }

    @Override
    public boolean saveChunk() {
        return event.isSaveChunk();
    }

    @Override
    public void saveChunk(boolean saveChunk) {
        event.setSaveChunk(saveChunk);
    }

    // on newer versions the event is not cancellable
    public boolean cancelled() {
        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }

    public void cancelled(boolean cancel) {
        if (event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(cancel);
        }
    }
}
