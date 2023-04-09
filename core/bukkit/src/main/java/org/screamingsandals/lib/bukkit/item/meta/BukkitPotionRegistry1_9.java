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
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitPotionRegistry1_9 extends BukkitPotionRegistry {
    // TODO: is there any bukkit-like server supporting custom values for this registry?

    public BukkitPotionRegistry1_9() {
        specialType(PotionType.class, BukkitPotion1_9::new);
        specialType(PotionData.class, BukkitPotion1_9::new);
    }

    @Override
    protected @Nullable Potion resolveMappingPlatform(@NotNull ResourceLocation location) {
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

            switch (path) {
                case "EMPTY":
                    path = "UNCRAFTABLE";
                    break;
                case "LEAPING":
                    path = "JUMP";
                    break;
                case "SWIFTNESS":
                    path = "SPEED";
                    break;
                case "HEALING":
                    path = "INSTANT_HEAL";
                    break;
                case "HARMING":
                    path = "INSTANT_DAMAGE";
                    break;
                case "REGENERATION":
                    path = "REGEN";
                    break;
            }

            var type = PotionType.valueOf(path);

            if (variant == 1) {
                if (type.isExtendable()) {
                    return new BukkitPotion1_9(new PotionData(type, true, false));
                }
            } else if (variant == 2) {
                if (type.isUpgradeable()) {
                    return new BukkitPotion1_9(new PotionData(type, false, true));
                }
            } else {
                return new BukkitPotion1_9(type);
            }
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Potion> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(PotionType.values()).flatMap(potion -> {
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
                BukkitPotion1_9::new,
                BukkitPotion1_9::constructKey,
                (potion, literal) -> BukkitPotion1_9.constructKey(potion).path().contains(literal),
                (potion, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
