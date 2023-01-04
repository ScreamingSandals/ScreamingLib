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

package org.screamingsandals.lib.plugin;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Wrapper;

import java.nio.file.Path;
import java.util.List;

@Data
public class PluginDescription implements Wrapper {
    private final @NotNull PluginKey pluginKey;
    private final @NotNull String name;
    private final @NotNull String version;
    private final @Nullable String description;
    private final @NotNull List<@NotNull String> authors;
    private final @NotNull List<@NotNull String> dependencies;
    private final @NotNull List<@NotNull String> softDependencies;
    private final @NotNull Path dataFolder;

    public @Nullable Object getInstance() {
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
        if (type.isInstance(instance)) {
            return (T) instance;
        }
        throw new UnsupportedOperationException("Can't convert instance to this type!");
    }
}
