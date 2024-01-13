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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.item.meta.PotionEffectTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface PotionEffectType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Contract(pure = true)
    default @NotNull PotionEffect asEffect() {
        return asEffect(0);
    }

    @Contract(pure = true)
    default @NotNull PotionEffect asEffect(int duration) {
        return asEffect(duration, 0);
    }

    @Contract(pure = true)
    default @NotNull PotionEffect asEffect(int duration, int amplifier) {
        return asEffect(duration, amplifier, true);
    }

    @Contract(pure = true)
    default @NotNull PotionEffect asEffect(int duration, int amplifier, boolean ambient) {
        return asEffect(duration, amplifier, ambient, true);
    }

    @Contract(pure = true)
    default @NotNull PotionEffect asEffect(int duration, int amplifier, boolean ambient, boolean particles) {
        return asEffect(duration, amplifier, ambient, particles, true);
    }

    @Contract(pure = true)
    @NotNull PotionEffect asEffect(int duration, int amplifier, boolean ambient, boolean particles, boolean icon);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull PotionEffectType of(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @NotNull Object effectType) {
        var result = ofNullable(effectType);
        Preconditions.checkNotNullIllegal(result, "Could not find potion effect type: " + effectType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable PotionEffectType ofNullable(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @Nullable Object effectType) {
        if (effectType instanceof PotionEffectType) {
            return (PotionEffectType) effectType;
        }
        return PotionEffectTypeRegistry.getInstance().resolveMapping(effectType);
    }

    static @NotNull RegistryItemStream<@NotNull PotionEffectType> all() {
        return PotionEffectTypeRegistry.getInstance().getRegistryItemStream();
    }
}
