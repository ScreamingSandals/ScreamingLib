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

package org.screamingsandals.lib.utils.config;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class JsonConfigurationLoaderBuilderSupplier implements ConfigurationLoaderBuilderSupplier {
    public static final @NotNull JsonConfigurationLoaderBuilderSupplier INSTANCE = new JsonConfigurationLoaderBuilderSupplier();

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?,?> get() throws UnsupportedOperationException {
        return get(true);
    }

    @Override
    public AbstractConfigurationLoader.@NotNull Builder<?, ?> getForLoadingOnly() throws UnsupportedOperationException {
        return get(false);
    }

    public AbstractConfigurationLoader.@NotNull Builder<?,?> get(boolean supportsSaving) throws UnsupportedOperationException {
        // good relocation plugins should be able to handle these class strings
        if (Reflect.has("org.spongepowered.configurate.jackson.JacksonConfigurationLoader")) {
            return JacksonConfigurationLoader.builder();
        } else if (Reflect.has("org.spongepowered.configurate.gson.GsonConfigurationLoader")) {
            return GsonConfigurationLoader.builder();
        } else if (!supportsSaving && Reflect.has("org.spongepowered.configurate.yaml.YamlConfigurationLoader")) {
            return YamlConfigurationLoader.builder(); // yes, it is able to load it
        } else {
            throw new UnsupportedOperationException("There is no Configurate Loader capable of loading json files!");
        }
    }
}
