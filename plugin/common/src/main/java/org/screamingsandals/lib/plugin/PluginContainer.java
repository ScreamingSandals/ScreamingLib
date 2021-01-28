package org.screamingsandals.lib.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Getter
public abstract class PluginContainer {
    private PluginDescription pluginDescription;
    private Logger logger;

    public void init(@NotNull PluginDescription pluginDescription, Logger logger) {
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

    public void load() {
    }

    public void enable() {
    }

    public void disable() {
    }
}
