package org.screamingsandals.lib.minestom.plugin;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.IExtensionObserver;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.plugin.event.PluginDisabledEvent;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.plugin.PluginDescription;
import org.screamingsandals.lib.plugin.PluginKey;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.internal.InternalEarlyInitialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@InternalEarlyInitialization
public class MinestomPluginManager extends PluginManager {
    private static final IExtensionObserver OBSERVER = extensionName -> {
        final var plugin = getPlugin(PluginManager.createKey(extensionName).orElseThrow());
        if (plugin.isPresent()) {
            EventManager.getDefaultEventManager().fireEvent(new PluginDisabledEvent(plugin.orElseThrow()));
        }
    };

    public static void init(Controllable controllable) {
        PluginManager.init(MinestomPluginManager::new);
        // TODO: check for extension load
        controllable.enable(() -> MinecraftServer.getExtensionManager().getExtensions().forEach(e -> e.observe(OBSERVER)));
    }

    @Override
    protected Optional<Object> getPlatformClass0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)));
    }

    @Override
    protected boolean isEnabled0(PluginKey pluginKey) {
        return MinecraftServer.getExtensionManager().hasExtension(pluginKey.as(String.class));
    }

    @Override
    protected Optional<PluginDescription> getPlugin0(PluginKey pluginKey) {
        return Optional.ofNullable(MinecraftServer.getExtensionManager().getExtension(pluginKey.as(String.class)))
                .map(this::wrap);
    }

    @Override
    protected List<PluginDescription> getAllPlugins0() {
        return MinecraftServer.getExtensionManager().getExtensions()
                .stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    @Override
    protected Optional<PluginKey> createKey0(Object identifier) {
        return Optional.of(MinestomPluginKey.of(identifier.toString()));
    }

    @Override
    protected PlatformType getPlatformType0() {
        return PlatformType.MINESTOM;
    }

    @Override
    protected Optional<PluginDescription> getPluginFromPlatformObject0(Object object) {
        return MinecraftServer.getExtensionManager().getExtensions()
                .stream()
                .filter(a -> a == object)
                .findFirst()
                .map(this::wrap);
    }

    private PluginDescription wrap(Extension extension) {
        return new PluginDescription(
                MinestomPluginKey.of(extension.getOrigin().getName()),
                extension.getOrigin().getName(),
                extension.getOrigin().getVersion(),
                "",
                Arrays.asList(extension.getOrigin().getAuthors()),
                new ArrayList<>(extension.getDependents()),
                List.of(),
                extension.getDataDirectory()
        );
    }
}
