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

package org.screamingsandals.lib.velocity.plugin;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.meta.PluginDependency;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@InternalEarlyInitialization
public class VelocityPluginManager extends PluginManager {
    private final com.velocitypowered.api.plugin.@NotNull PluginManager pluginManager;

    @Override
    protected @Nullable Object getPlatformClass0(@NotNull PluginKey pluginKey) {
        return pluginManager.getPlugin(pluginKey.as(String.class)).orElse(null);
    }

    @Override
    protected boolean isEnabled0(@NotNull PluginKey pluginKey) {
        return pluginManager.isLoaded(pluginKey.as(String.class));
    }

    @Override
    protected @Nullable PluginDescription getPlugin0(@NotNull PluginKey pluginKey) {
        return pluginManager.getPlugin(pluginKey.as(String.class)).map(this::wrap).orElse(null);
    }

    @Override
    protected @NotNull List<@NotNull PluginDescription> getAllPlugins0() {
        return pluginManager.getPlugins().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected @Nullable PluginKey createKey0(@NotNull Object identifier) {
        return VelocityPluginKey.of(identifier.toString());
    }

    @Override
    protected @NotNull PlatformType getPlatformType0() {
        return PlatformType.VELOCITY;
    }

    @Override
    protected @Nullable PluginDescription getPluginFromPlatformObject0(@NotNull Object object) {
        return  pluginManager.getPlugins()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap)
                .orElse(null);
    }

    private @NotNull PluginDescription wrap(@NotNull PluginContainer plugin) {
        var description = plugin.getDescription();
        return new PluginDescription(
                VelocityPluginKey.of(description.getId()),
                description.getName().orElse(description.getId()),
                description.getVersion().orElse("unknown"),
                description.getDescription().orElse(""),
                description.getAuthors(),
                description.getDependencies().stream().map(PluginDependency::getId).collect(Collectors.toList()),
                List.of(),
                description.getSource().map(path -> path.getParent().resolve(description.getId())).orElse(Path.of("."))
        );
    }
}
