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

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface Potion extends RegistryItem {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.POTION) @Nullable Object @NotNull... objects);

    static @NotNull Potion of(@MinecraftType(MinecraftType.Type.POTION) @NotNull Object potion) {
        var result = ofNullable(potion);
        Preconditions.checkNotNullIllegal(result, "Could not find potion: " + potion);
        return result;
    }

    @Contract("null -> null")
    static @Nullable Potion ofNullable(@MinecraftType(MinecraftType.Type.POTION) @Nullable Object potion) {
        if (potion instanceof Potion) {
            return (Potion) potion;
        }
        return PotionRegistry.getInstance().resolveMapping(potion);
    }

    static @NotNull RegistryItemStream<@NotNull Potion> all() {
        return PotionRegistry.getInstance().getRegistryItemStream();
    }
}
