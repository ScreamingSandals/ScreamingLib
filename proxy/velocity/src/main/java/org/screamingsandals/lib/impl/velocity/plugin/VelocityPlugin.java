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

package org.screamingsandals.lib.impl.velocity.plugin;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.meta.PluginDependency;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.plugin.Plugin;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class VelocityPlugin extends BasicWrapper<PluginContainer> implements Plugin {
    protected VelocityPlugin(@NotNull PluginContainer wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable Object getInstance() {
        return wrappedObject;
    }

    @Override
    public boolean isEnabled() {
        return true; // TODO: is this true?
    }

    @Override
    public @NotNull String pluginKey() {
        return wrappedObject.getDescription().getId();
    }

    @Override
    public @NotNull String name() {
        var description = wrappedObject.getDescription();
        return description.getName().orElse(description.getId());
    }

    @Override
    public @NotNull String version() {
        return wrappedObject.getDescription().getVersion().orElse("unknown");
    }

    @Override
    public @Nullable String description() {
        return wrappedObject.getDescription().getDescription().orElse(null);
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Contributor> contributors() {
        return wrappedObject.getDescription().getAuthors().stream().map(VelocityContributor::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Dependency> dependencies() {
        return wrappedObject.getDescription().getDependencies()
                .stream()
                .map(VelocityDependency::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull Path dataFolder() {
        var description = wrappedObject.getDescription();
        return description.getSource().map(path -> path.getParent().resolve(description.getId())).orElse(Path.of("."));
    }

    @Data
    @Accessors(fluent = true)
    public static class VelocityContributor implements Contributor {
        private final @NotNull String name;
    }

    @Data
    @Accessors(fluent = true)
    public static class VelocityDependency implements Dependency {
        private final @NotNull PluginDependency pluginDependency;

        @Override
        public @NotNull String pluginKey() {
            return pluginDependency.getId();
        }

        @Override
        public boolean required() {
            return pluginDependency.isOptional();
        }

        @Override
        public @NotNull LoadOrder loadOrder() {
            return LoadOrder.AFTER;
        }

        @Override
        public @Nullable String requiredVersion() {
            return pluginDependency.getVersion().orElse(null);
        }
    }
}
