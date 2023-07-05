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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.util.Locale;
import java.util.function.Supplier;

@FunctionalInterface
public interface ConfigurationLoaderBuilderSupplier extends Supplier<AbstractConfigurationLoader.@NotNull Builder<?,?>> {

    /**
     * This method returns a builder which creates a loader which is capable of LOADING the specific file format and SAVING.
     *
     * @return a builder which builds a loader for the specific file format
     * @throws UnsupportedOperationException if there is no loader capable of loading and saving the file format
     */
    @Override
    AbstractConfigurationLoader.@NotNull Builder<?, ?> get() throws UnsupportedOperationException;

    /**
     * This method returns a builder which creates a loader which is capable of LOADING the specific file format.
     * <p>
     * The loader may NOT be able to SAVE the file in a correct format (for example, SnakeYaml is capable of loading JSON files, but can only save YAML files).
     *
     * @return a builder which builds a loader for the specific file format
     * @throws UnsupportedOperationException if there is no loader capable of loading the file format
     */
    default AbstractConfigurationLoader.@NotNull Builder<?,?> getForLoadingOnly() throws UnsupportedOperationException {
        return get();
    }

    /**
     * This method finds and returns a builder supplier based on the specified file extension.
     *
     * @param extension the file extension
     * @return a builder supplier for the specified extension or null if no such builder supplier exists
     */
    static @Nullable ConfigurationLoaderBuilderSupplier getForExtension(@NotNull String extension) {
        switch (extension.toLowerCase(Locale.ROOT)) {
            case "yml":
            case "yaml":
                return YamlConfigurationLoaderBuilderSupplier.INSTANCE;
            case "json":
                return JsonConfigurationLoaderBuilderSupplier.INSTANCE;
            case "xml":
                return XmlConfigurationLoaderBuilderSupplier.INSTANCE;
            case "conf":
            case "hocon":
                return HoconConfigurationLoaderBuilderSupplier.INSTANCE;
            default:
                return null;
        }
    }
}
