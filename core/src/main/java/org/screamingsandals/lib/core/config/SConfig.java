package org.screamingsandals.lib.core.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.screamingsandals.lib.core.config.adapter.GsonConfigAdapter;
import org.screamingsandals.lib.core.config.adapter.HoconConfigAdapter;
import org.screamingsandals.lib.core.config.adapter.YamlConfigAdapter;
import org.screamingsandals.lib.core.config.exception.SConfigException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Simple adapter for various libraries
 */
public interface SConfig {
    static SConfig create(Format format, Path path) throws SConfigException {
        switch (format) {
            case GSON:
                return new GsonConfigAdapter(path);
            case HOCON:
                return new HoconConfigAdapter(path);
            case YAML:
                return new YamlConfigAdapter(path);
            default:
                throw new SConfigException("Bad config format! " + format.name());
        }
    }
    /**
     * Main configuration node
     *
     * @return config node
     */
    CommentedConfigurationNode getRoot();

    /**
     * Config node by key
     *
     * @return config node
     */
    CommentedConfigurationNode get(String key);

    /**
     * Location of the config
     * @return path to the config file
     */
    Path getPath();

    /**
     * Saves the current config
     */
    void save() throws SConfigException;

    /**
     * In case something changed in the file, we need to load it
     */
    void load() throws SConfigException;

    /**
     * converts current node
     * @param loader
     * @param path
     * @param format
     */
    default void convert(ConfigurationLoader<?> loader, Path path, Format format) throws SConfigException {
        final ConfigurationLoader<?> newLoader;
        switch (format) {
            case YAML:
                newLoader = YAMLConfigurationLoader.builder()
                        .setPath(path)
                        .build();
                break;
            case HOCON:
                newLoader = HoconConfigurationLoader.builder()
                        .setPath(path)
                        .build();
                break;
            case GSON:
                newLoader = GsonConfigurationLoader.builder()
                        .setPath(path)
                        .build();
                break;
            default:
                newLoader = null;
                break;
        }

        try {
            newLoader.save(loader.load());
        } catch (IOException e) {
            throw new SConfigException("Oopsie, converting failed..", e);
        }
    }

    /**
     * Supported formats:
     * - YAML
     * - HOCON
     * - JSON
     */
    enum Format {
        YAML,
        HOCON,
        GSON
    }
}
