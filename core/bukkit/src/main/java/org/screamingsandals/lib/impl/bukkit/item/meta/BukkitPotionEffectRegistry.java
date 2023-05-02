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

package org.screamingsandals.lib.impl.bukkit.item.meta;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.screamingsandals.lib.impl.item.meta.PotionEffectRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitPotionEffectRegistry extends PotionEffectRegistry {
    @SuppressWarnings("ConstantConditions")
    public BukkitPotionEffectRegistry() {
        specialType(PotionEffectType.class, BukkitPotionEffect::new);
        specialType(org.bukkit.potion.PotionEffect.class, BukkitPotionEffect::new);
    }

    @Override
    protected @Nullable PotionEffect resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (BukkitFeature.POTION_EFFECT_TYPE_REGISTRY.isSupported()) {
            var entityType = Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitPotionEffect(entityType);
            }
        } else if (BukkitFeature.POTION_EFFECT_KEYED.isSupported()) {
            // Spigot and pre-1.18.2 Paper don't have registries for this, but have method that works the similar way
            var entityType = PotionEffectType.getByKey(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitPotionEffect(entityType);
            }
        } else {
            // hell nah
            if (!"minecraft".equals(location.namespace())) {
                return null;
            }

            var path = location.path();
            switch (path) {
                //<editor-fold desc="1.18 flattening names -> Enum constants" defaultstate="collapsed">
                // @formatter:off

                //case "speed": path = "SPEED"; break; - same
                case "slowness": path = "SLOW"; break;
                case "haste": path = "FAST_DIGGING"; break;
                case "mining_fatigue": path = "SLOW_DIGGING"; break;
                case "strength": path = "INCREASE_DAMAGE"; break;
                case "instant_health": path = "HEAL"; break;
                case "instant_damage": path = "HARM"; break;
                case "jump_boost": path = "JUMP"; break;
                case "nausea": path = "CONFUSION"; break;
                //case "regeneration": path = "REGENERATION"; break; - same
                case "resistance": path = "DAMAGE_RESISTANCE"; break;
                //case "fire_resistance": path = "FIRE_RESISTANCE"; break; - same
                //case "water_breathing": path = "WATER_BREATHING"; break; - same
                //case "invisibility": path = "INVISIBILITY"; break; - same
                //case "blindness": path = "BLINDNESS"; break; - same
                //case "night_vision": path = "NIGHT_VISION"; break; - same
                //case "hunger": path = "HUNGER"; break; - same
                //case "weakness": path = "WEAKNESS"; break; - same
                //case "poison": path = "POISON"; break; - same
                //case "wither": path = "WITHER"; break; - same
                //case "health_boost": path = "HEALTH_BOOST"; break; - same
                //case "absorption": path = "ABSORPTION"; break; - same
                //case "saturation": path = "SATURATION"; break; - same
                //case "glowing": path = "GLOWING"; break; - same
                //case "levitation": path = "LEVITATION"; break; - same
                //case "luck": path = "LUCK"; break; - same
                //case "unluck": path = "UNLUCK"; break; - same
                //case "slow_falling": path = "SLOW_FALLING"; break; - same
                //case "conduit_power": path = "CONDUIT_POWER"; break; - same
                //case "dolphins_grace": path = "DOLPHINS_GRACE"; break; - same
                //case "bad_omen": path = "BAD_OMEN"; break; - same
                //case "hero_of_the_village": path = "HERO_OF_THE_VILLAGE"; break; - same

                // @formatter:on
                //</editor-fold>
            }
            var value = PotionEffectType.getByName(path.toUpperCase(Locale.ROOT));
            if (value != null) {
                return new BukkitPotionEffect(value);
            }
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull PotionEffect> getRegistryItemStream0() {
        if (BukkitFeature.POTION_EFFECT_KEYED.isSupported()) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(PotionEffectType.values()),
                    BukkitPotionEffect::new,
                    potionEffect -> ResourceLocation.of(potionEffect.getKey().getNamespace(), potionEffect.getKey().getKey()),
                    (potionEffect, literal) -> potionEffect.getKey().getKey().contains(literal),
                    (potionEffect, namespace) -> potionEffect.getKey().getNamespace().equals(namespace),
                    List.of()
            );
        } else {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(PotionEffectType.values()),
                    BukkitPotionEffect::new,
                    potionEffect -> ResourceLocation.of(BukkitPotionEffect.getLocationPath(potionEffect)),
                    (potionEffect, literal) -> BukkitPotionEffect.getLocationPath(potionEffect).contains(literal),
                    (potionEffect, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }
}
