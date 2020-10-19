package org.screamingsandals.lib.core.plugin;

import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.lib.core.player.PlayerWrapper;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public interface PluginCore {
    Logger getLog();

    <P> P getPlugin();

    String getPluginName();

    default ServerType getType() {
        if (getPlugin() instanceof net.md_5.bungee.api.plugin.Plugin) {
            return ServerType.WATERFALL;
        }
        return ServerType.PAPER;
    }

    File getPluginFolder();

    Path getPluginFolderPath();

    void sendMessage(TextComponent component);

   Optional< PlayerWrapper<?>> getWrapperFor(UUID uuid);

   boolean isPluginEnabled(String pluginName);

    enum ServerType {
        PAPER,
        WATERFALL
    }
}
