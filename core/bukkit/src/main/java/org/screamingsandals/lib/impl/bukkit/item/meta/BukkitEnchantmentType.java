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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;

public class BukkitEnchantmentType extends BasicWrapper<org.bukkit.enchantments.Enchantment> implements EnchantmentType {

    public BukkitEnchantmentType(@NotNull org.bukkit.enchantments.Enchantment enchantment) {
        super(enchantment);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull Enchantment asEnchantment(int level) {
        return new BukkitEnchantment(wrappedObject, level);
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.enchantments.Enchantment) {
            return wrappedObject.equals(object);
        }
        if (object instanceof EnchantmentType) {
            return equals(object);
        }
        return equals(EnchantmentType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if (BukkitFeature.FLATTENING.isSupported()) {
            var bukkitKey = wrappedObject.getKey();
            return ResourceLocation.of(bukkitKey.getNamespace(), bukkitKey.getKey());
        } else {
            return ResourceLocation.of(getLocationPath(wrappedObject));
        }
    }

    public static @NotNull String getLocationPath(@NotNull org.bukkit.enchantments.Enchantment enchantment) {
        String path = enchantment.getName();
        switch (path) {
            //<editor-fold desc="Enum constants -> 1.13 flattening names" defaultstate="collapsed">
            // @formatter:off

            case "PROTECTION_ENVIRONMENTAL": path = "protection"; break;
            case "PROTECTION_FIRE": path = "fire_protection"; break;
            case "PROTECTION_FALL": path = "feather_falling"; break;
            case "PROTECTION_EXPLOSIONS": path = "blast_protection"; break;
            case "PROTECTION_PROJECTILE": path = "projectile_protection"; break;
            case "OXYGEN": path = "respiration"; break;
            case "WATER_WORKER": path = "aqua_affinity"; break;
            //case "THORNS": path = "thorns"; break; - same
            //case "DEPTH_STRIDER": path = "depth_strider"; break; - same
            //case "FROST_WALKER": path = "frost_walker"; break; - same
            //case "BINDING_CURSE": path = "binding_curse"; break; - same
            case "DAMAGE_ALL": path = "sharpness"; break;
            case "DAMAGE_UNDEAD": path = "smite"; break;
            case "DAMAGE_ARTHROPODS": path = "bane_of_arthropods"; break;
            //case "KNOCKBACK": path = "knockback"; break; - same
            //case "FIRE_ASPECT": path = "fire_aspect"; break; - same
            case "LOOT_BONUS_MOBS": path = "looting"; break;
            case "SWEEPING_EDGE": path = "sweeping"; break;
            case "DIG_SPEED": path = "efficiency"; break;
            //case "SILK_TOUCH": path = "silk_touch"; break; - same
            case "DURABILITY": path = "unbreaking"; break;
            case "LOOT_BONUS_BLOCKS": path = "fortune"; break;
            case "ARROW_DAMAGE": path = "power"; break;
            case "ARROW_KNOCKBACK": path = "punch"; break;
            case "ARROW_FIRE": path = "flame"; break;
            case "ARROW_INFINITE": path = "infinity"; break;
            case "LUCK": path = "luck_of_the_sea"; break;
            //case "LURE": path = "lure"; break; - same
            //case "MENDING": path = "mending"; break; - same
            //case "VANISHING_CURSE": path = "vanishing_curse"; break; - same

            // @formatter:on
            //</editor-fold>
        }
        return path.toLowerCase(Locale.ROOT);
    }
}
