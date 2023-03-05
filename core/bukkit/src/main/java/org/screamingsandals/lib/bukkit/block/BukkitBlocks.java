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

package org.screamingsandals.lib.bukkit.block;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.ext.paperlib.PaperLib;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

@Service
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitBlocks extends Blocks {

    public BukkitBlocks() {
        converter.registerP2W(org.bukkit.Location.class, location -> {
                    final var block = location.getBlock();
                    final var instanced = Locations.wrapLocation(block.getLocation());
                    if (!Version.isVersion(1,13)) {
                        return new Block(instanced, BlockTypeHolder.of(block.getState().getData()));
                    } else {
                        return new Block(instanced, BlockTypeHolder.of(block.getBlockData()));
                    }
                })
                .registerP2W(org.bukkit.block.Block.class, block -> resolve(block.getLocation()).orElseThrow())
                .registerP2W(Location.class, location -> resolve(location.as(org.bukkit.Location.class)).orElseThrow())
                .registerW2P(org.bukkit.block.Block.class, holder -> {
                    final var location = holder.getLocation().as(org.bukkit.Location.class);
                    return location.getBlock();
                });

        if (Reflect.has("org.bukkit.block.data.BlockData")) {
           converter.registerW2P(BlockData.class, holder -> holder.getType().as(BlockData.class));
        }
    }

    @Override
    protected @NotNull Block getBlockAt0(@NotNull Location location) {
        return resolve(location).orElseThrow();
    }

    @Override
    protected void setBlockAt0(@NotNull Location location, @NotNull BlockTypeHolder material, boolean ignorePhysics) {
        final var bukkitLocation = location.as(org.bukkit.Location.class);
        PaperLib.getChunkAtAsync(bukkitLocation)
                .thenAccept(result -> {
                    if (!Version.isVersion(1,13)) {
                        bukkitLocation.getBlock().setType(material.as(Material.class), !ignorePhysics);
                        Reflect.getMethod(bukkitLocation.getBlock(), "setData", byte.class, boolean.class).invoke(material.legacyData(), !ignorePhysics);
                    } else {
                        bukkitLocation.getBlock().setBlockData(material.as(BlockData.class), !ignorePhysics);
                    }
                });
    }

    @Override
    protected void breakNaturally0(@NotNull Location location) {
        location.as(org.bukkit.Location.class).getBlock().breakNaturally();
    }
}
