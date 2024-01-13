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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BukkitProfessionRegistry1_8 extends BukkitProfessionRegistry {
    public BukkitProfessionRegistry1_8() {
        specialType(Villager.Profession.class, BukkitProfession1_8::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Profession> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(Villager.Profession.values()).filter(profession -> !"NORMAL".equals(profession.name()) && !"HUSK".equals(profession.name())), // filter out unused values
                BukkitProfession1_8::new,
                profession -> {
                    if ("BLACKSMITH".equals(profession.name())) {
                        return ResourceLocation.of("toolsmith");
                    } else if ("PRIEST".equals(profession.name())) {
                        return ResourceLocation.of("cleric");
                    }

                    return ResourceLocation.of(profession.name());
                },
                (profession, literal) -> {
                    var path = profession.name().toLowerCase(Locale.ROOT);

                    if ("blacksmith".equals(path)) {
                        if ("toolsmith".contains(literal)) {
                            return true;
                        }
                    } else if ("priest".equals(path)) {
                        if ("cleric".contains(literal)) {
                            return true;
                        }
                    }

                    return path.contains(literal);
                },
                (profession, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable Profession resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var enumName = location.path();

            if ("toolsmith".equalsIgnoreCase(enumName)) {
                enumName = "BLACKSMITH";
            } else if ("cleric".equalsIgnoreCase(enumName)) {
                enumName = "PRIEST";
            }

            var value = Villager.Profession.valueOf(enumName.toUpperCase(Locale.ROOT));
            return new BukkitProfession1_8(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }
}
