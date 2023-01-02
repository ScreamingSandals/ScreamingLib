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

package org.screamingsandals.lib.bukkit.world.difficulty;

import org.bukkit.Difficulty;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.difficulty.DifficultyMapping;

import java.util.Arrays;

@Service
public class BukkitDifficultyMapping extends DifficultyMapping {
    public BukkitDifficultyMapping() {
        difficultyConverter
                .registerP2W(Difficulty.class, BukkitDifficultyHolder::new);

        Arrays.stream(Difficulty.values()).forEach(difficulty -> {
            var holder = new BukkitDifficultyHolder(difficulty);
            mapping.put(NamespacedMappingKey.of(difficulty.name()), holder);
            values.add(holder);
        });
    }
}
