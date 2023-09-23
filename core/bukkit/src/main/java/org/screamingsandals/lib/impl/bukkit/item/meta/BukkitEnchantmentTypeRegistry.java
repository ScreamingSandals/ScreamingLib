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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.item.meta.EnchantmentTypeRegistry;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.item.meta.EnchantmentType;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitEnchantmentTypeRegistry extends EnchantmentTypeRegistry {

    public BukkitEnchantmentTypeRegistry() {
        specialType(org.bukkit.enchantments.Enchantment.class, BukkitEnchantmentType::new);
    }

    @Override
    protected @Nullable EnchantmentType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (BukkitFeature.REGISTRY.isSupported()) {
            var entityType = Registry.ENCHANTMENT.get(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitEnchantmentType(entityType);
            }


            // try bukkit name (deprecated, TODO: prepare shop/config migration scripts and remove)
            if ("minecraft".equals(location.namespace())) {
                var value = org.bukkit.enchantments.Enchantment.getByName(location.path().toUpperCase(Locale.ROOT));
                if (value != null) {
                    return new BukkitEnchantmentType(value);
                }
            }
        } else if (BukkitFeature.FLATTENING.isSupported()) {
            // 1.13.x didn't have registries yet, but we got something similar to them in this case
            var entityType = org.bukkit.enchantments.Enchantment.getByKey(new NamespacedKey(location.namespace(), location.path()));
            if (entityType != null) {
                return new BukkitEnchantmentType(entityType);
            }


            // try bukkit name (deprecated, TODO: prepare shop/config migration scripts and remove)
            if ("minecraft".equals(location.namespace())) {
                var value = org.bukkit.enchantments.Enchantment.getByName(location.path().toUpperCase(Locale.ROOT));
                if (value != null) {
                    return new BukkitEnchantmentType(value);
                }
            }
        } else {
            // hell nah
            if (!"minecraft".equals(location.namespace())) {
                return null;
            }

            var path = location.path();
            switch (path) {
                //<editor-fold desc="1.13 flattening names -> Enum constants" defaultstate="collapsed">
                // @formatter:off

                case "protection": path = "PROTECTION_ENVIRONMENTAL"; break;
                case "fire_protection": path = "PROTECTION_FIRE"; break;
                case "feather_falling": path = "PROTECTION_FALL"; break;
                case "blast_protection": path = "PROTECTION_EXPLOSIONS"; break;
                case "projectile_protection": path = "PROTECTION_PROJECTILE"; break;
                case "respiration": path = "OXYGEN"; break;
                case "aqua_affinity": path = "WATER_WORKER"; break;
                //case "thorns": path = "THORNS"; break; - same
                //case "depth_strider": path = "DEPTH_STRIDER"; break; - same
                //case "frost_walker": path = "FROST_WALKER"; break; - same
                //case "binding_curse": path = "BINDING_CURSE"; break; - same
                case "sharpness": path = "DAMAGE_ALL"; break;
                case "smite": path = "DAMAGE_UNDEAD"; break;
                case "bane_of_arthropods": path = "DAMAGE_ARTHROPODS"; break;
                //case "knockback": path = "KNOCKBACK"; break; - same
                //case "fire_aspect": path = "FIRE_ASPECT"; break; - same
                case "looting": path = "LOOT_BONUS_MOBS"; break;
                case "sweeping": path = "SWEEPING_EDGE"; break;
                case "efficiency": path = "DIG_SPEED"; break;
                //case "silk_touch": path = "SILK_TOUCH"; break; - same
                case "unbreaking": path = "DURABILITY"; break;
                case "fortune": path = "LOOT_BONUS_BLOCKS"; break;
                case "power": path = "ARROW_DAMAGE"; break;
                case "punch": path = "ARROW_KNOCKBACK"; break;
                case "flame": path = "ARROW_FIRE"; break;
                case "infinity": path = "ARROW_INFINITE"; break;
                case "luck_of_the_sea": path = "LUCK"; break;
                //case "lure": path = "LURE"; break; - same
                //case "mending": path = "MENDING"; break; - same
                //case "vanishing_curse": path = "VANISHING_CURSE"; break; - same

                // @formatter:on
                //</editor-fold>
            }
            var value = org.bukkit.enchantments.Enchantment.getByName(path.toUpperCase(Locale.ROOT));
            if (value != null) {
                return new BukkitEnchantmentType(value);
            }
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EnchantmentType> getRegistryItemStream0() {
        if (BukkitFeature.FLATTENING.isSupported()) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.enchantments.Enchantment.values()),
                    BukkitEnchantmentType::new,
                    enchantment -> ResourceLocation.of(enchantment.getKey().getNamespace(), enchantment.getKey().getKey()),
                    (enchantment, literal) -> enchantment.getKey().getKey().contains(literal),
                    (enchantment, namespace) -> enchantment.getKey().getNamespace().equals(namespace),
                    List.of()
            );
        } else {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(org.bukkit.enchantments.Enchantment.values()),
                    BukkitEnchantmentType::new,
                    enchantment -> ResourceLocation.of(BukkitEnchantmentType.getLocationPath(enchantment)),
                    (enchantment, literal) -> BukkitEnchantmentType.getLocationPath(enchantment).contains(literal),
                    (enchantment, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }
}
