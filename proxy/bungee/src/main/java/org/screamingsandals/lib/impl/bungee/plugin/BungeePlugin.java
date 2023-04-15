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

package org.screamingsandals.lib.impl.bungee.plugin;

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
import java.util.stream.Stream;

public class BungeePlugin extends BasicWrapper<net.md_5.bungee.api.plugin.Plugin> implements Plugin {
    protected BungeePlugin(net.md_5.bungee.api.plugin.@NotNull Plugin wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable Object getInstance() {
        return wrappedObject;
    }

    @Override
    public boolean isEnabled() {
        return true; // it is enabled because it exists :)
    }

    @Override
    public @NotNull String pluginKey() {
        return wrappedObject.getDescription().getName(); // name and plugin key is the same
    }

    @Override
    public @NotNull String name() {
        return wrappedObject.getDescription().getName();
    }

    @Override
    public @NotNull String version() {
        var version = wrappedObject.getDescription().getVersion();
        return version != null ? version : "unknown";
    }

    @Override
    public @Nullable String description() {
        return wrappedObject.getDescription().getDescription();
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Contributor> contributors() {
        var author = wrappedObject.getDescription().getAuthor();
        if (author == null) {
            return List.of();
        }
        return List.of(new BungeeContributor(author));
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Dependency> dependencies() {
        var description = wrappedObject.getDescription();
        var depend = description.getDepends();
        var softDepend = description.getSoftDepends();

        // remove duplicates
        softDepend.removeAll(depend);

        var dependStream = depend.stream().map(s -> new BungeeDependency(s, true));
        var softDependStream = softDepend.stream().map(s -> new BungeeDependency(s, false));

        return Stream.concat(dependStream, softDependStream).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull Path dataFolder() {
        return wrappedObject.getDataFolder().toPath().toAbsolutePath();
    }

    @Data
    @Accessors(fluent = true)
    public static class BungeeContributor implements Contributor {
        private final @NotNull String name;
    }

    @Data
    @Accessors(fluent = true)
    public static class BungeeDependency implements Dependency {
        private final @NotNull String pluginKey;
        private final boolean required;

        @Override
        public @NotNull LoadOrder loadOrder() {
            return LoadOrder.AFTER;
        }

        @Override
        public @Nullable String requiredVersion() {
            return null;
        }
    }
}
