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

package org.screamingsandals.lib.bukkit.world.chunk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.World;
import org.screamingsandals.lib.world.Worlds;
import org.screamingsandals.lib.world.chunk.Chunk;

import java.util.Arrays;
import java.util.Objects;

public class BukkitChunk extends BasicWrapper<org.bukkit.Chunk> implements Chunk {
    public BukkitChunk(@NotNull org.bukkit.Chunk wrappedObject) {
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
    public @NotNull World getWorld() {
        return Worlds.wrapWorld(wrappedObject.getWorld());
    }

    @Override
    public @NotNull Block getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z) {
        return new BukkitBlock(wrappedObject.getBlock(x, y, z));
    }

    @Override
    public @NotNull BasicEntity @NotNull [] getEntities() {
        return Arrays.stream(wrappedObject.getEntities())
                .map(Entities::wrapEntity)
                .filter(Objects::nonNull)
                .toArray(BasicEntity[]::new);
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
