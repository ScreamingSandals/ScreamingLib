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
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;
import org.screamingsandals.lib.utils.Wrapper;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Getter
public abstract class PluginContainer implements Wrapper {
    private PluginDescription pluginDescription;
    private LoggerWrapper logger;

    @ApiStatus.Internal
    public void init(@NotNull PluginDescription pluginDescription, LoggerWrapper logger) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.getName() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
        this.logger = logger;
    }

    // stolen from bukkit api ;)
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if ("".equals(resourcePath)) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        var in = getClass().getResourceAsStream("/" + resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in classpath");
        }

        var outFile = pluginDescription.getDataFolder().resolve(resourcePath).toFile();
        var outDir = outFile.getParentFile();

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                var out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                getLogger().warn("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            getLogger().error("Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    public Path getDataFolder() {
        return getPluginDescription().getDataFolder();
    }

    @ApiStatus.Experimental
    public Logger getSLF4JLogger() {
        return logger.as(Logger.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return getPluginDescription().as(type);
    }

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

    @OnPostEnable
    @ApiStatus.OverrideOnly
    public void preDisable() {
    }

    @OnDisable
    @ApiStatus.OverrideOnly
    public void disable() {
    }
}
