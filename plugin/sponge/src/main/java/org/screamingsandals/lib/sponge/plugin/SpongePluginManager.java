package org.screamingsandals.lib.sponge.plugin;

import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginContributor;
import org.spongepowered.plugin.metadata.PluginDependency;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PlatformMapping(platform = PlatformType.SPONGE)
public class SpongePluginManager extends PluginManager {

    public static void init() {
        PluginManager.init(SpongePluginManager::new);
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        // that weird map o -> o is needed cause compiler is on some drugs
        return Sponge.getPluginManager().getPlugin(pluginKey.as(String.class)).map(o -> o);
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return Sponge.getPluginManager().getPlugin(pluginKey.as(String.class)).isPresent();
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Sponge.getPluginManager().getPlugin(pluginKey.as(String.class)).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return Sponge.getPluginManager().getPlugins().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(SpongePluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.SPONGE;
    }

    private PluginDescription wrap(PluginContainer plugin) {
        return new PluginDescription(
                SpongePluginKey.of(plugin.getMetadata().getId()),
                plugin.getMetadata().getName().orElse(plugin.getMetadata().getId()),
                plugin.getMetadata().getVersion(),
                plugin.getMetadata().getDescription().orElse(""),
                plugin.getMetadata().getContributors().stream().map(PluginContributor::getName).collect(Collectors.toList()),
                plugin.getMetadata().getDependencies().stream().map(PluginDependency::getId).collect(Collectors.toList()),
                List.of(),
                plugin.getPath()
        );
    }
}
