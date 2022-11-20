/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.plugin;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Data
public class PluginDescription implements Wrapper {
    private final PluginKey pluginKey;
    private final String name;
    private final String version;
    private final String description;
    private final List<String> authors;
    private final List<String> dependencies;
    private final List<String> softDependencies;
    private final Path dataFolder;

    public Optional<Object> getInstance() {
        return PluginManager.getPlatformClass(this.pluginKey);
    }

    public boolean isEnabled() {
        return PluginManager.isEnabled(this.pluginKey);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        var instance = PluginManager.getPlatformClass(this.pluginKey);
        if (instance.isPresent() && type.isInstance(instance.get())) {
            return (T) instance.get();
        }
        throw new UnsupportedOperationException("Can't convert instance to this type!");
    }
}
