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

package org.screamingsandals.lib.world.weather;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface WeatherType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.WEATHER) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.WEATHER) @Nullable Object @NotNull... objects);

    static @NotNull WeatherType of(@MinecraftType(MinecraftType.Type.WEATHER) @NotNull Object weather) {
        var result = ofNullable(weather);
        Preconditions.checkNotNullIllegal(result, "Could not find weather: " + weather);
        return result;
    }

    @Contract("null -> null")
    static @Nullable WeatherType ofNullable(@MinecraftType(MinecraftType.Type.WEATHER) @Nullable Object weather) {
        if (weather instanceof WeatherType) {
            return (WeatherType) weather;
        }
        return WeatherRegistry.getInstance().resolveMapping(weather);
    }

    static @NotNull RegistryItemStream<@NotNull WeatherType> all() {
        return WeatherRegistry.getInstance().getRegistryItemStream();
    }
}
