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

package org.screamingsandals.lib.impl.bukkit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.DyeColor;
import org.screamingsandals.lib.impl.DyeColorRegistry;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitDyeColorRegistry extends DyeColorRegistry {
    public BukkitDyeColorRegistry() {
        specialType(org.bukkit.DyeColor.class, BukkitDyeColor::new);
    }

    // TODO: is there any bukkit-like server supporting custom values for this registry?
    @Override
    protected @Nullable DyeColor resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var path = location.path();

            if (!BukkitFeature.DYE_COLOR_LIGHT_GRAY_SUPPORTED.isSupported()) {
                if ("light_gray".equals(path)) {
                    path = "silver";
                }
            }

            var value = org.bukkit.DyeColor.valueOf(path.toUpperCase(Locale.ROOT));
            return new BukkitDyeColor(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull DyeColor> getRegistryItemStream0() {
        if (BukkitFeature.DYE_COLOR_LIGHT_GRAY_SUPPORTED.isSupported()) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.DyeColor.values()),
                    BukkitDyeColor::new,
                    dyeColor -> ResourceLocation.of(dyeColor.name()),
                    (dyeColor, literal) -> dyeColor.name().toLowerCase(Locale.ROOT).contains(literal),
                    (dyeColor, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        } else {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.DyeColor.values()),
                    BukkitDyeColor::new,
                    dyeColor -> {
                        if ("SILVER".equals(dyeColor.name())) {
                            return ResourceLocation.of("light_gray");
                        }
                        return ResourceLocation.of(dyeColor.name());
                    },
                    (dyeColor, literal) -> {
                        var path = dyeColor.name().toLowerCase(Locale.ROOT);
                        if ("silver".equals(path)) {
                            path = "light_gray";
                        }
                        return path.contains(literal);
                    },
                    (dyeColor, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }
}
