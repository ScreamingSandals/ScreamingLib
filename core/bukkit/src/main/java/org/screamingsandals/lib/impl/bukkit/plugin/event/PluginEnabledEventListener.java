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

package org.screamingsandals.lib.impl.bukkit.plugin.event;

import lombok.experimental.ExtensionMethod;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.impl.bukkit.plugin.BukkitPlugin;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.plugin.event.PluginEnabledEvent;
import org.screamingsandals.lib.utils.extensions.NullableExtension;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class PluginEnabledEventListener extends AbstractBukkitEventHandlerFactory<PluginEnableEvent, PluginEnabledEvent> {
    public PluginEnabledEventListener(@NotNull Plugin plugin) {
        super(PluginEnableEvent.class, PluginEnabledEvent.class, plugin);
    }

    @Override
    protected @Nullable PluginEnabledEvent wrapEvent(@NotNull PluginEnableEvent event, @NotNull EventPriority priority) {
        return () -> new BukkitPlugin(event.getPlugin());
    }
}
