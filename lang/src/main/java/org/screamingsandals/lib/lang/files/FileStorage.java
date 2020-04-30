package org.screamingsandals.lib.lang.files;

import lombok.Data;
import org.screamingsandals.lib.config.ConfigAdapter;
import org.screamingsandals.lib.lang.Language;
import org.screamingsandals.lib.lang.Utils;
import org.screamingsandals.lib.lang.storage.Storage;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

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
        final List<String> filesInFolder = Language.getResourceFolderFiles("languages");
        filesInFolder.forEach(this::registerLanguage);

        final HashMap<String, Storage> availableLanguages = new HashMap<>();
        for (var key : languageFiles.keySet()) {
            ConfigAdapter config = languageFiles.get(key);
            Storage storage = new Storage(config, key, fallbackStorages.getOrDefault(key, null));

            availableLanguages.put(key, storage);
        }

        if (availableLanguages.isEmpty()) {
            return false;
        }

        Language.getInstance().setAvailableLanguages(new HashMap<>(availableLanguages));
        return true;
    }

    public void registerLanguage(String languagePath) {
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

    public void registerFallbackStorage(String path) {
        ConfigAdapter configAdapter = loadFromInputStream(getClass().getResourceAsStream(path));

        final String key = configAdapter.getString("language_code");
        fallbackStorages.put(key, new Storage(configAdapter, key));
    }

    private ConfigAdapter loadFromFile(File file) {
        final ConfigAdapter config = Utils.createConfigFile(file);
        config.load();

        return config;
    }

    private ConfigAdapter loadFromInputStream(InputStream inputStream) {
        final ConfigAdapter config = Utils.createConfigInputStream(inputStream);
        config.load();

        return config;
    }
}
