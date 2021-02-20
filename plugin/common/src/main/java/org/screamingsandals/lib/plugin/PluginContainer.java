package org.screamingsandals.lib.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.plugin.logger.LoggerWrapper;
import org.screamingsandals.lib.utils.Wrapper;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Getter
public abstract class PluginContainer implements Wrapper {
    private PluginDescription pluginDescription;
    private LoggerWrapper logger;

    public void init(@NotNull PluginDescription pluginDescription, LoggerWrapper logger) {
        if (this.pluginDescription != null) {
            throw new UnsupportedOperationException(pluginDescription.getName() + " is already initialized!");
        }
        this.pluginDescription = pluginDescription;
        this.logger = logger;
    }

    // stolen from bukkit api ;)
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        var in = getClass().getResourceAsStream(resourcePath);
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

    public Logger getSLF4JLogger() {
        return logger.as(Logger.class);
    }

    @Override
    public <T> T as(Class<T> type) {
        return getPluginDescription().as(type);
    }

    public void load() {
    }

    public void enable() {
    }

    public void disable() {
    }
}
