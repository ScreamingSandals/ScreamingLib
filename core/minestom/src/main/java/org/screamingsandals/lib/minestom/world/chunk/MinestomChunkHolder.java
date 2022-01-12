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

package org.screamingsandals.lib.minestom.world.chunk;

import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.minestom.world.MinestomLocationMapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

import java.util.Optional;

public class MinestomChunkHolder extends BasicWrapper<Chunk> implements ChunkHolder {
    protected MinestomChunkHolder(Chunk wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getX() {
        return wrappedObject.getChunkX();
    }

    @Override
    public int getZ() {
        return wrappedObject.getChunkZ();
    }

    @Override
    public WorldHolder getWorld() {
        return WorldMapper.wrapWorld(wrappedObject.getInstance());
    }

    @Override
    public BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z) {
        return MinestomLocationMapper.wrapPoint(wrappedObject.toPosition().add(x, y, z), getWorld()).getBlock();
    }

    @Override
    public EntityBasic[] getEntities() {
        return wrappedObject.getInstance().getChunkEntities(wrappedObject).stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(EntityBasic[]::new);
    }

    @Override
    public boolean isLoaded() {
        return wrappedObject.isLoaded();
    }

    @Override
    public boolean load() {
        wrappedObject.getInstance().loadOptionalChunk(wrappedObject.getChunkX(), wrappedObject.getChunkZ());
        return true;
    }

    @Override
    public boolean load(boolean generate) {
        if (generate) {
            wrappedObject.getInstance().loadChunk(wrappedObject.getChunkX(), wrappedObject.getChunkZ());
            return true;
        }
        return load();
    }

    @Override
    public boolean unload() {
        wrappedObject.getInstance().unloadChunk(wrappedObject);
        return true;
    }

    @Override
    public boolean unload(boolean save) {
        unload();
        if (save) {
            wrappedObject.getInstance().saveChunkToStorage(wrappedObject);
        }
        return true;
    }
}
