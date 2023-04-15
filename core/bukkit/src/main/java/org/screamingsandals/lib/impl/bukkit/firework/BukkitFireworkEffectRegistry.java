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

package org.screamingsandals.lib.impl.bukkit.firework;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.firework.FireworkEffectRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.impl.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitFireworkEffectRegistry extends FireworkEffectRegistry {
    public BukkitFireworkEffectRegistry() {
        specialType(org.bukkit.FireworkEffect.Type.class, BukkitFireworkEffect::new);
        specialType(org.bukkit.FireworkEffect.class, BukkitFireworkEffect::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull FireworkEffect> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.FireworkEffect.Type.values()),
                BukkitFireworkEffect::new,
                fireworkEffect -> ResourceLocation.of(BukkitFireworkEffect.getLocationPath(fireworkEffect)),
                (fireworkEffect, literal) -> BukkitFireworkEffect.getLocationPath(fireworkEffect).contains(literal),
                (fireworkEffect, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable FireworkEffect resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var path = location.path();
            if ("small_ball".equalsIgnoreCase(path)) {
                path = "BALL";
            } else if ("large_ball".equalsIgnoreCase(path)) {
                path = "BALL_LARGE";
            }

            var value = org.bukkit.FireworkEffect.Type.valueOf(path.toUpperCase(Locale.ROOT));
            return new BukkitFireworkEffect(value);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }
}
