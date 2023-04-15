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

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitPotion1_9 extends BasicWrapper<PotionData> implements Potion {

    public BukkitPotion1_9(@NotNull PotionType type) {
        this(new PotionData(type));
    }

    public BukkitPotion1_9(@NotNull PotionData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        if (wrappedObject.isExtended()) {
            return "LONG_" + wrappedObject.getType().name();
        } else if (wrappedObject.isUpgraded()) {
            return "STRONG_" + wrappedObject.getType().name();
        }
        return wrappedObject.getType().name();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof PotionData || object instanceof Potion) {
            return equals(object);
        }
        return equals(Potion.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == PotionType.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return constructKey(wrappedObject);
    }

    public static @NotNull ResourceLocation constructKey(@NotNull PotionData data) {
        var bukkitName = data.getType().name();
        String name;
        switch (bukkitName) {
            case "UNCRAFTABLE":
                name = "EMPTY";
                break;
            case "JUMP":
                name = "LEAPING";
                break;
            case "SPEED":
                name = "SWIFTNESS";
                break;
            case "INSTANT_HEAL":
                name = "HEALING";
                break;
            case "INSTANT_DAMAGE":
                name = "HARMING";
                break;
            case "REGEN":
                name = "REGENERATION";
                break;
            default:
                name = bukkitName;
        }

        if (data.isExtended()) {
            name = "STRONG_" + name;
        } else if (data.isUpgraded()) {
            name = "LONG_" + name;
        }

        return ResourceLocation.of(name);
    }
}
