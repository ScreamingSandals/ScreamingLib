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

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.methods.OnDisable;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPluginLoad;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;
import org.screamingsandals.lib.api.Wrapper;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Deprecated
public abstract class PluginContainer implements Wrapper {
    private Plugin pluginDescription;
    private LoggerWrapper logger;

    @ApiStatus.Internal
    @OnPostConstruct
    public final void init(@NotNull Plugin pluginDescription, @NotNull LoggerWrapper logger) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.name() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
        this.logger = logger;
    }

    public void saveResource(@NotNull String resourcePath, boolean replace) {
        PluginUtils.saveResource(getPluginDescription(), getLogger(), resourcePath, replace);
    }

    public @NotNull Path getDataFolder() {
        return getPluginDescription().dataFolder();
    }

    @ApiStatus.Experimental
    public @NotNull Logger getSLF4JLogger() {
        return logger.as(Logger.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return getPluginDescription().as(type);
    }

    @OnPluginLoad
    @ApiStatus.OverrideOnly
    public void load() {
    }

    @OnEnable
    @ApiStatus.OverrideOnly
    public void enable() {
    }

    @OnPostEnable
    @ApiStatus.OverrideOnly
    public void postEnable() {
    }

    @OnPreDisable
    @ApiStatus.OverrideOnly
    public void preDisable() {
    }

    @OnDisable
    @ApiStatus.OverrideOnly
    public void disable() {
    }
}
