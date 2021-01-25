package org.screamingsandals.lib.plugin;

import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class PluginManager {
    private static PluginManager pluginManager;

    public static void init(Supplier<PluginManager> supplier) {
        if (pluginManager != null) {
            throw new UnsupportedOperationException("PluginManager is already initialized.");
        }

        pluginManager = supplier.get();
    }

    public static Optional<Object> getPlatformClass(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformClass0(pluginKey);
    }

    protected abstract Optional<Object> getPlatformClass0(PluginKey pluginKey);

    public static boolean isEnabled(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.isEnabled0(pluginKey);
    }

    protected abstract boolean isEnabled0(PluginKey pluginKey);

    public static Optional<PluginDescription> getPlugin(PluginKey pluginKey) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlugin0(pluginKey);
    }

    protected abstract Optional<PluginDescription> getPlugin0(PluginKey pluginKey);

    public static List<PluginDescription> getAllPlugins() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getAllPlugins0();
    }

    protected abstract List<PluginDescription> getAllPlugins0();

    /**
     * Creates key that can be used to access plugin.
     * Any valid identifiers will create PluginKey, it doesn't matter, if the plugin really exists.
     *
     * @param identifier platform identifier
     * @return optional with PluginKey
     */
    public static Optional<PluginKey> createKey(Object identifier) {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.createKey0(identifier);
    }

    protected abstract Optional<PluginKey> createKey0(Object identifier);

    public static PlatformType getPlatformType() {
        if (pluginManager == null) {
            throw new UnsupportedOperationException("PluginManager is not initialized yet.");
        }
        return pluginManager.getPlatformType0();
    }

    protected abstract PlatformType getPlatformType0();
}
