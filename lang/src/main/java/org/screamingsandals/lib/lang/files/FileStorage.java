package org.screamingsandals.lib.lang.files;

import lombok.Data;
import org.screamingsandals.lib.config.ConfigAdapter;
import org.screamingsandals.lib.lang.Base;
import org.screamingsandals.lib.lang.Utils;
import org.screamingsandals.lib.lang.storage.Storage;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ScreamingSandals team
 */
@Data
public class FileStorage {
    private final Object plugin;
    private final HashMap<String, ConfigAdapter> languageFiles = new HashMap<>();
    private final HashMap<String, Storage> fallbackStorages = new HashMap<>();

    public FileStorage(Object plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        final List<String> filesInFolder = Base.getResourceFolderFiles("languages");
        filesInFolder.forEach(this::registerLanguage);

        final Map<String, Storage> availableLanguages = new HashMap<>();
        for (var key : languageFiles.keySet()) {
            final ConfigAdapter config = languageFiles.get(key);
            final Storage storage = new Storage(config, key, fallbackStorages.getOrDefault(key, null));

            availableLanguages.put(key, storage);
        }

        if (availableLanguages.isEmpty()) {
            return false;
        }

        Base.getInstance().setAvailableLanguages(new HashMap<>(availableLanguages));
        return true;
    }

    private void registerLanguage(String languagePath) {
        final File dataFolder = Utils.getDataFolder(plugin);
        final String languagePathIS = "/" + languagePath;
        ConfigAdapter configAdapter;

        if (dataFolder != null && dataFolder.exists()) {
            final File langFile = new File(dataFolder, languagePath);
            if (langFile.exists()) {
                configAdapter = loadFromFile(langFile);
                registerFallbackStorage(languagePathIS);
            } else {
                configAdapter = loadFromInputStream(getClass().getResourceAsStream(languagePathIS));
            }
        } else {
            configAdapter = loadFromInputStream(getClass().getResourceAsStream(languagePathIS));
        }

        final String key = configAdapter.getString("language_code");
        languageFiles.put(key, configAdapter);
    }

    private void registerFallbackStorage(String path) {
        ConfigAdapter configAdapter = loadFromInputStream(getClass().getResourceAsStream(path));

        final String key = configAdapter.getString("language_code");
        fallbackStorages.put(key, new Storage(configAdapter, key));
    }

    private ConfigAdapter loadFromFile(File file) {
        final var config = Utils.createConfigFile(file);
        config.load();

        return config;
    }

    private ConfigAdapter loadFromInputStream(InputStream inputStream) {
        final var config = Utils.createConfigInputStream(inputStream);
        config.load();

        return config;
    }
}
