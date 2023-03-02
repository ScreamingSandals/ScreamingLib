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

package org.screamingsandals.lib.bukkit.plugin;

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

public class BukkitPlugin extends BasicWrapper<org.bukkit.plugin.Plugin> implements Plugin {
    public BukkitPlugin(@NotNull org.bukkit.plugin.Plugin wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable Object getInstance() {
        return wrappedObject;
    }

    @Override
    public boolean isEnabled() {
        return wrappedObject.isEnabled();
    }

    @Override
    public @NotNull String pluginKey() {
        return wrappedObject.getName(); // name and plugin key is the same
    }

    @Override
    public @NotNull String name() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull String version() {
        return wrappedObject.getDescription().getVersion();
    }

    @Override
    public @Nullable String description() {
        return wrappedObject.getDescription().getDescription();
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Contributor> contributors() {
        return wrappedObject.getDescription().getAuthors().stream().map(BukkitContributor::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Dependency> dependencies() {
        // TODO: implement Paper's PluginMeta here after the new information are added to it

        var description = wrappedObject.getDescription();
        var depend = description.getDepend();
        var softDepend = description.getSoftDepend();
        var loadBefore = description.getLoadBefore();

        // remove duplicates
        softDepend.removeAll(depend);
        loadBefore.removeAll(depend);
        softDepend.removeAll(loadBefore);

        var dependStream = depend.stream().map(s -> new BukkitDependency(s, true, Dependency.LoadOrder.AFTER));
        var softDependStream = softDepend.stream().map(s -> new BukkitDependency(s, false, Dependency.LoadOrder.AFTER));
        var loadBeforeStream = loadBefore.stream().map(s -> new BukkitDependency(s, false, Dependency.LoadOrder.BEFORE));

        return Stream.of(dependStream, softDependStream, loadBeforeStream).flatMap(s -> s).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull Path dataFolder() {
        return wrappedObject.getDataFolder().toPath().toAbsolutePath();
    }

    @Data
    @Accessors(fluent = true)
    public static class BukkitContributor implements Contributor {
        private final @NotNull String name;
    }

    @Data
    @Accessors(fluent = true)
    public static class BukkitDependency implements Dependency {
        private final @NotNull String pluginKey;
        private final boolean required;
        private final @NotNull LoadOrder loadOrder;

        @Override
        public @Nullable String requiredVersion() {
            return null;
        }
    }
}
