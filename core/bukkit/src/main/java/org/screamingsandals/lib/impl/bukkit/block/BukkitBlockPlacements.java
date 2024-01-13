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

package org.screamingsandals.lib.impl.bukkit.block;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.block.BlockPlacements;
import org.screamingsandals.lib.world.Location;

import java.util.Objects;

@Service
public class BukkitBlockPlacements extends BlockPlacements {

    public BukkitBlockPlacements() {
        converter.registerP2W(org.bukkit.Location.class, location -> new BukkitBlockPlacement(location.getBlock()))
                .registerP2W(org.bukkit.block.Block.class, block -> Objects.requireNonNull(resolve(block.getLocation())));
    }

    @Override
    protected @NotNull BlockPlacement getBlockAt0(@NotNull Location location) {
        var bukkitLocation = location.as(org.bukkit.Location.class);
        return new BukkitBlockPlacement(bukkitLocation.getBlock());
    }
}
