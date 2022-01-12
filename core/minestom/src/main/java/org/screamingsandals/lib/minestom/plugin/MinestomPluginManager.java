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

package org.screamingsandals.lib.minestom.plugin;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class MinestomPluginManager extends PluginManager {
    public static void init(Controllable controllable) {
        PluginManager.init(MinestomPluginManager::new);
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return MinecraftServer.getExtensionManager().hasExtension(pluginKey.as(String.class));
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)))
                .map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return MinecraftServer.getExtensionManager().getExtensions()
                .stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(MinestomPluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.MINESTOM;
    }

    @Override
    protected Optional<PluginDescription> getPluginFromPlatformObject0(Object object) {
        return MinecraftServer.getExtensionManager().getExtensions()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap);
    }

    private PluginDescription wrap(Extension extension) {
        return new PluginDescription(
                MinestomPluginKey.of(extension.getOrigin().getName()),
                extension.getOrigin().getName(),
                extension.getOrigin().getVersion(),
                "",
                Arrays.asList(extension.getOrigin().getAuthors()),
                new ArrayList<>(extension.getDependents()),
                List.of(),
                extension.getDataDirectory()
        );
    }
}
