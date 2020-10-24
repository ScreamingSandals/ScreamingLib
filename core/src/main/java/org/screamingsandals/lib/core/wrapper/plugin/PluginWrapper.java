package org.screamingsandals.lib.core.wrapper.plugin;

import org.screamingsandals.lib.core.wrapper.sender.PlayerWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public interface PluginWrapper {
    Logger getLog();

    <P> P getPlugin();

    String getPluginName();

    default ServerType getType() {
        if (getPlugin() instanceof net.md_5.bungee.api.plugin.Plugin) {
            return ServerType.BUNGEE;
        }
        return ServerType.BUKKIT;
    }

    File getPluginFolder();

    Path getPluginFolderPath();

    SenderWrapper<?> getConsoleWrapper();

    Optional<PlayerWrapper<?>> getWrapperFor(UUID uuid);

    boolean isPluginEnabled(String pluginName);

    enum ServerType {
        BUKKIT,
        BUNGEE
    }
}
