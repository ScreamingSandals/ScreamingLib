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
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.item.meta.Enchantment;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;

public class BukkitEnchantment extends BasicWrapper<Pair<org.bukkit.enchantments.Enchantment, Integer>> implements Enchantment {

    public BukkitEnchantment(@NotNull org.bukkit.enchantments.Enchantment enchantment) {
        this(Pair.of(enchantment, 1));
    }

    public BukkitEnchantment(@NotNull Pair<org.bukkit.enchantments.@NotNull Enchantment, @NotNull Integer> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.first().getName();
    }

    @Override
    public int level() {
        return wrappedObject.second();
    }

    @Override
    public @NotNull Enchantment withLevel(int level) {
        return new BukkitEnchantment(Pair.of(wrappedObject.first(), level));
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.enchantments.Enchantment) {
            return wrappedObject.equals(Pair.of(object, 1));
        }
        if (object instanceof Enchantment) {
            return equals(object);
        }
        return equals(Enchantment.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public boolean isSameType(@Nullable Object object) {
        if (object instanceof org.bukkit.enchantments.Enchantment) {
            return wrappedObject.first().equals(object);
        } else if (object instanceof BukkitEnchantment) {
            return ((BukkitEnchantment) object).wrappedObject.first().equals(wrappedObject.first());
        }
        var other = Enchantment.ofNullable(object);
        return other != null && platformName().equals(other.platformName());
    }

    @Override
    public boolean isSameType(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::isSameType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == org.bukkit.enchantments.Enchantment.class) {
            return (T) wrappedObject.first();
        }
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if (Server.isVersion(1, 13)) {
            var bukkitKey = wrappedObject.first().getKey();
            return ResourceLocation.of(bukkitKey.getNamespace(), bukkitKey.getKey());
        } else {
            return ResourceLocation.of(getLocationPath(wrappedObject.first()));
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
