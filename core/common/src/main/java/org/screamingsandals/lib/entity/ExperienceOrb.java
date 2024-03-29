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

package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.world.Location;

import java.util.function.Consumer;

/**
 * Represents an entity with identifier {@code minecraft:experience_orb}.
 */
public interface ExperienceOrb extends Entity {
    int getExperience();

    void setExperience(int experience);

    static @Nullable ExperienceOrb dropExperience(int experience, @NotNull Location location) {
        return Entities.dropExperience(experience, location);
    }

    static @Nullable ExperienceOrb dropExperience(int experience, @NotNull Location location, @Nullable Consumer<? super @NotNull ExperienceOrb> preSpawnFunction) {
        return Entities.dropExperience(experience, location, preSpawnFunction);
    }
}
