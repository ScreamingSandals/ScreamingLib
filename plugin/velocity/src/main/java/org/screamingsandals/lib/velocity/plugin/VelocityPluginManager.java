package org.screamingsandals.lib.velocity.plugin;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.meta.PluginDependency;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VelocityPluginManager extends PluginManager {
    private final com.velocitypowered.api.plugin.PluginManager pluginManager;

    public static void init(com.velocitypowered.api.plugin.PluginManager pluginManager) {
        PluginManager.init(() -> new VelocityPluginManager(pluginManager));
    }

    @Override
    protected Object getPlatformClass0(PluginKey pluginKey) {
        // I don't need optional here (probably I'll change that later
        return pluginManager.getPlugin(pluginKey.as(String.class)).orElse(null);
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

    private PluginDescription wrap(PluginContainer plugin) {
        return new PluginDescription(
                VelocityPluginKey.of(plugin.getDescription().getId()),
                plugin.getDescription().getName().orElse(plugin.getDescription().getId()),
                plugin.getDescription().getVersion().orElse(""),
                plugin.getDescription().getDescription().orElse(""),
                plugin.getDescription().getAuthors(),
                plugin.getDescription().getDependencies().stream().map(PluginDependency::getId).collect(Collectors.toList()),
                List.of(),
                null // TODO
        );
    }
}
