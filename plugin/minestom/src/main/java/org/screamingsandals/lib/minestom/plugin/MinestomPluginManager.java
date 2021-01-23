package org.screamingsandals.lib.minestom.plugin;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PlatformMapping(platform = PlatformType.MINESTOM)
public class MinestomPluginManager extends PluginManager {

    public static void init() {
        PluginManager.init(MinestomPluginManager::new);
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)) != null;
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class))).map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return MinecraftServer.getExtensionManager().getExtensions().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(MinestomPluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.MINESTOM;
    }

    private PluginDescription wrap(Extension extension) {
        return new PluginDescription(
                MinestomPluginKey.of(extension.getDescription().getName()),
                extension.getDescription().getName(),
                extension.getDescription().getVersion(),
                "",
                extension.getDescription().getAuthors(),
                extension.getDescription().getDependents(),
                List.of(),
                // TODO: check if this is the right directory
                MinecraftServer.getExtensionManager().getExtensionFolder().toPath().resolve(extension.getDescription().getName())
        );
    }
}
