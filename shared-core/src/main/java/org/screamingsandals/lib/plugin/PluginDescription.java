package org.screamingsandals.lib.plugin;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Data
public class PluginDescription implements Wrapper {
    private final PluginKey pluginKey;
    private final String name;
    private final String version;
    private final String description;
    private final List<String> authors;
    private final List<String> dependencies;
    private final List<String> softDependencies;
    private final Path dataFolder;

    public Optional<Object> getInstance() {
        return PluginManager.getPlatformClass(this.pluginKey);
    }

    public boolean isEnabled() {
        return PluginManager.isEnabled(this.pluginKey);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        var instance = PluginManager.getPlatformClass(this.pluginKey);
        if (instance.isPresent() && type.isInstance(instance.get())) {
            return (T) instance.get();
        }
        throw new UnsupportedOperationException("Can't convert instance to this type!");
    }
}
