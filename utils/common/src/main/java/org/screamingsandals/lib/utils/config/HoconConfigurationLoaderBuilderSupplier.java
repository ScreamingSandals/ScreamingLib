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

package org.screamingsandals.lib.utils.config;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

public class HoconConfigurationLoaderBuilderSupplier implements ConfigurationLoaderBuilderSupplier {
    public static final @NotNull HoconConfigurationLoaderBuilderSupplier INSTANCE = new HoconConfigurationLoaderBuilderSupplier();

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?,?> get() throws UnsupportedOperationException {
        // good relocation plugins should be able to handle these class strings
        if (Reflect.has("org.spongepowered.configurate.hocon.HoconConfigurationLoader")) {
            return HoconConfigurationLoader.builder();
        } else {
            throw new UnsupportedOperationException("There is no Configurate Loader capable of loading xml files!");
        }
    }
}