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

package org.screamingsandals.lib.impl.bukkit.world.difficulty;

import org.bukkit.Difficulty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.impl.world.difficulty.DifficultyRegistry;
import org.screamingsandals.lib.world.difficulty.DifficultyType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitDifficultyRegistry extends DifficultyRegistry {
    public BukkitDifficultyRegistry() {
        specialType(Difficulty.class, BukkitDifficultyType::new);
    }

    // TODO: is there any bukkit-like server supporting custom values for this registry?
    @Override
    protected @Nullable DifficultyType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var value = Difficulty.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitDifficultyType(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull DifficultyType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(Difficulty.values()),
                BukkitDifficultyType::new,
                difficulty -> ResourceLocation.of(difficulty.name()),
                (difficulty, literal) -> difficulty.name().toLowerCase(Locale.ROOT).contains(literal),
                (difficulty, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
