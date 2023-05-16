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

package org.screamingsandals.lib.firework;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.firework.FireworkEffectTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface FireworkEffectType extends RegistryItem, RawValueHolder {

    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_TYPE) @Nullable Object @NotNull... objects);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_TYPE) @Nullable Object object);

    static @NotNull FireworkEffectType of(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_TYPE) @NotNull Object effectType) {
        var result = ofNullable(effectType);
        Preconditions.checkNotNullIllegal(result, "Could not find firework effect type: " + effectType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable FireworkEffectType ofNullable(@MinecraftType(MinecraftType.Type.FIREWORK_EFFECT_TYPE) @Nullable Object effectType) {
        if (effectType instanceof FireworkEffectType) {
            return (FireworkEffectType) effectType;
        }
        return FireworkEffectTypeRegistry.getInstance().resolveMapping(effectType);
    }

    static @NotNull RegistryItemStream<@NotNull FireworkEffectType> all() {
        return FireworkEffectTypeRegistry.getInstance().getRegistryItemStream();
    }
}
