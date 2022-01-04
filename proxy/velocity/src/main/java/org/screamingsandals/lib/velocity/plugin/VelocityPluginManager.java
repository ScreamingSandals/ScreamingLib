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

package org.screamingsandals.lib.velocity.plugin;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.meta.PluginDependency;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@InternalEarlyInitialization
public class VelocityPluginManager extends PluginManager {
    private final com.velocitypowered.api.plugin.PluginManager pluginManager;

    public static void init(com.velocitypowered.api.plugin.PluginManager pluginManager) {
        PluginManager.init(() -> new VelocityPluginManager(pluginManager));
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        // that weird map o -> o is needed cause compiler is on some drugs
        return pluginManager.getPlugin(pluginKey.as(String.class)).map(o -> o);
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return pluginManager.isLoaded(pluginKey.as(String.class));
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return pluginManager.getPlugin(pluginKey.as(String.class)).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return pluginManager.getPlugins().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(VelocityPluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.VELOCITY;
    }

    @Override
    protected Optional<PluginDescription> getPluginFromPlatformObject0(Object object) {
        return  pluginManager.getPlugins()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap);
    }

    private PluginDescription wrap(PluginContainer plugin) {
        return new PluginDescription(
                VelocityPluginKey.of(plugin.getDescription().getId()),
                plugin.getDescription().getName().orElse(plugin.getDescription().getId()),
                plugin.getDescription().getVersion().orElse(""),
                plugin.getDescription().getDescription().orElse(""),
                plugin.getDescription().getAuthors(),
                plugin.getDescription().getDependencies().stream().map(PluginDependency::getId).collect(Collectors.toList()),
                List.of(),
                plugin.getDescription().getSource().map(path -> path.getParent().resolve(plugin.getDescription().getId())).orElse(Path.of("."))
        );
    }
}
