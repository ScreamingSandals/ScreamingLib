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

package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.item.meta.PotionEffectRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface PotionEffect extends RegistryItem {
    @NotNull String platformName();

    int duration();

    int amplifier();

    boolean ambient();

    boolean particles();

    boolean icon();

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withDuration(int duration);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withAmplifier(int amplifier);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withAmbient(boolean ambient);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withParticles(boolean particles);

    @Contract(value = "_ -> new", pure = true)
    @NotNull PotionEffect withIcon(boolean icon);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION_EFFECT) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION_EFFECT) @Nullable Object @NotNull... objects);

    boolean isSameType(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @Nullable Object object);

    boolean isSameType(@MinecraftType(MinecraftType.Type.POTION_EFFECT_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull PotionEffect of(@MinecraftType(MinecraftType.Type.POTION_EFFECT) @NotNull Object effect) {
        var result = ofNullable(effect);
        Preconditions.checkNotNullIllegal(result, "Could not find potion effect: " + effect);
        return result;
    }

    @Contract("null -> null")
    static @Nullable PotionEffect ofNullable(@MinecraftType(MinecraftType.Type.POTION_EFFECT) @Nullable Object effect) {
        if (effect instanceof PotionEffect) {
            return (PotionEffect) effect;
        }
        return PotionEffectRegistry.getInstance().resolveMapping(effect);
    }

    static @NotNull RegistryItemStream<@NotNull PotionEffect> all() {
        return PotionEffectRegistry.getInstance().getRegistryItemStream();
    }
}
