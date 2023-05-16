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

package org.screamingsandals.lib.impl.bukkit.firework;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.firework.FireworkEffectType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitFireworkEffectType extends BasicWrapper<org.bukkit.FireworkEffect.Type> implements FireworkEffectType {
    public BukkitFireworkEffectType(org.bukkit.FireworkEffect.@NotNull Type type) {
        super(type);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.FireworkEffect.Type || object instanceof FireworkEffectType) {
            return equals(object);
        }
        return equals(FireworkEffectType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(getLocationPath(wrappedObject));
    }

    public static @NotNull String getLocationPath(org.bukkit.FireworkEffect.@NotNull Type type) {
        if (type == org.bukkit.FireworkEffect.Type.BALL) {
            return "small_ball";
        } else if (type == org.bukkit.FireworkEffect.Type.BALL_LARGE) {
            return "large_ball";
        }
        return type.name();
    }
}
