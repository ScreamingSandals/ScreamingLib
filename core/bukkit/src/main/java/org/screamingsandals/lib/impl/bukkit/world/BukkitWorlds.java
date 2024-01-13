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

package org.screamingsandals.lib.impl.bukkit.world;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.World;
import org.screamingsandals.lib.world.Worlds;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BukkitWorlds extends Worlds {
    @Override
    protected @Nullable World getWorld0(@NotNull UUID uuid) {
        var world = Bukkit.getWorld(uuid);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    @Override
    protected @Nullable World getWorld0(@NotNull String name) {
        try {
            return getWorld0(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
        }
        var world = Bukkit.getWorld(name);
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    @Override
    protected @NotNull List<@NotNull World> getWorlds0() {
        return Bukkit.getWorlds().stream()
                .map(BukkitWorld::new)
                .collect(Collectors.toList());
    }
}
