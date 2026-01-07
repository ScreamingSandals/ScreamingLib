/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.compat.v1_21_3;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.utils.ColorUtils;
import org.screamingsandals.lib.particle.Trail;

@UtilityClass
public class TargetColorCompat {
    public static @NotNull Object convertTargetColor(@NotNull Trail trail) {
        return new Particle.TargetColor(
                trail.location().as(Location.class),
                ColorUtils.getBukkitColor(trail.color())
        );
    }
}
