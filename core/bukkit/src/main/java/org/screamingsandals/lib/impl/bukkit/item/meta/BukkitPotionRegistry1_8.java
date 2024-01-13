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

package org.screamingsandals.lib.impl.bukkit.item.meta;

import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitPotionRegistry1_8 extends BukkitPotionRegistry {
    public BukkitPotionRegistry1_8() {
        specialType(PotionType.class, BukkitPotion1_8::new);
        specialType(org.bukkit.potion.Potion.class, BukkitPotion1_8::new);
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
                if (type != PotionType.WATER && type != PotionType.INSTANT_DAMAGE && type != PotionType.INSTANT_HEAL) {
                    return new BukkitPotion1_8(new org.bukkit.potion.Potion(type, 1, false, true));
                }
            } else if (variant == 2) {
                if (type.getMaxLevel() >= 2) {
                    return new BukkitPotion1_8(new org.bukkit.potion.Potion(type, 2, false, false));
                }
            } else {
                return new BukkitPotion1_8(type);
            }
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Potion> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(PotionType.values()).flatMap(potion -> {
                    var results = new ArrayList<org.bukkit.potion.Potion>();
                    results.add(new org.bukkit.potion.Potion(potion));
                    if (potion != PotionType.WATER && potion != PotionType.INSTANT_DAMAGE && potion != PotionType.INSTANT_HEAL) {
                        results.add(new org.bukkit.potion.Potion(potion, 1, false, true));
                    }
                    if (potion.getMaxLevel() >= 2) {
                        results.add(new org.bukkit.potion.Potion(potion, 2, false, false));
                    }
                    return results.stream();
                }),
                BukkitPotion1_8::new,
                BukkitPotion1_8::constructKey,
                (potion, literal) -> BukkitPotion1_8.constructKey(potion).path().contains(literal),
                (potion, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
