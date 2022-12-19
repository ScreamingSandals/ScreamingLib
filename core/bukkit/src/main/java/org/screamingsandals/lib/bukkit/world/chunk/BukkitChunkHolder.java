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

package org.screamingsandals.lib.bukkit.world.chunk;

import org.bukkit.Chunk;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

import java.util.Arrays;
import java.util.Objects;

public class BukkitChunkHolder extends BasicWrapper<Chunk> implements ChunkHolder {
    public BukkitChunkHolder(Chunk wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getX() {
        return wrappedObject.getX();
    }

    @Override
    public int getZ() {
        return wrappedObject.getZ();
    }

    @Override
    public WorldHolder getWorld() {
        return WorldMapper.wrapWorld(wrappedObject.getWorld());
    }

    @Override
    public BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z) {
        return BlockMapper.wrapBlock(wrappedObject.getBlock(x, y, z));
    }

    @Override
    public EntityBasic[] getEntities() {
        return Arrays.stream(wrappedObject.getEntities())
                .map(EntityMapper::wrapEntity)
                .filter(Objects::nonNull)
                .toArray(EntityBasic[]::new);
    }

    @Override
    public boolean isLoaded() {
        return wrappedObject.isLoaded();
    }

    @Override
    public boolean load() {
        return wrappedObject.load();
    }

    @Override
    public boolean load(boolean generate) {
        return wrappedObject.load(generate);
    }

    @Override
    public boolean unload() {
        return wrappedObject.unload();
    }

    @Override
    public boolean unload(boolean save) {
        return wrappedObject.unload(save);
    }
}
