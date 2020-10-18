package org.screamingsandals.lib.core;

import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

public interface PluginCore {
    Logger getLog();

    <P> P getPlugin();

    String getPluginName();

    ServerType getType();

    File getPluginFolder();

    Path getPluginFolderPath();

    enum ServerType {
        PAPER,
        WATERFALL
    }
}
