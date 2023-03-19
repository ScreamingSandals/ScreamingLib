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

package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.item.meta.PotionRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitPotionRegistry extends PotionRegistry {
    private static final boolean IS_POTION_SUPPORTED = Version.isVersion(1, 9);

    // TODO: is there any bukkit-like server supporting custom values for this registry?

    public BukkitPotionRegistry() {
        if (IS_POTION_SUPPORTED) {
            specialType(PotionType.class, BukkitPotion::new);
            specialType(PotionData.class, BukkitPotion::new);
        } else {
            // TODO: 1.8 support
        }
    }

    @Override
    protected @Nullable Potion resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!IS_POTION_SUPPORTED) {
            // TODO: 1.8 support
            return null;
        }

        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var path = location.path().toUpperCase(Locale.ROOT);
            byte variant = 0;
            if (path.startsWith("LONG_")) {
                path = path.substring(5);
                variant = 1;
            } else if (path.startsWith("STRONG_")) {
                path = path.substring(7);
                variant = 2;
            }

            var type = org.bukkit.potion.PotionType.valueOf(path);

            if (variant == 1) {
                if (type.isExtendable()) {
                    return new BukkitPotion(new PotionData(type, true, false));
                }
            } else if (variant == 2) {
                if (type.isUpgradeable()) {
                    return new BukkitPotion(new PotionData(type, false, true));
                }
            } else {
                return new BukkitPotion(type);
            }
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Potion> getRegistryItemStream0() {
        if (!IS_POTION_SUPPORTED) {
            // TODO: 1.8 support
            return SimpleRegistryItemStream.createDummy();
        }

        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.potion.PotionType.values()).flatMap(potion -> {
                    var results = new ArrayList<PotionData>();
                    results.add(new PotionData(potion));
                    if (potion.isExtendable()) {
                        results.add(new PotionData(potion, true, false));
                    }
                    if (potion.isUpgradeable()) {
                        results.add(new PotionData(potion, false, true));
                    }
                    return results.stream();
                }),
                BukkitPotion::new,
                BukkitPotion::constructKey,
                (potion, literal) -> BukkitPotion.constructKey(potion).path().contains(literal),
                (potion, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
