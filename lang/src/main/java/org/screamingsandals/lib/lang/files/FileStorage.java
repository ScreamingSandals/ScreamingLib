package org.screamingsandals.lib.lang.files;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.config.BungeeConfigAdapter;
import org.screamingsandals.lib.config.ConfigAdapter;
import org.screamingsandals.lib.config.SpigotConfigAdapter;
import org.screamingsandals.lib.lang.Language;
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
    private Storage fallbackStorage;

    public FileStorage(Object plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        List<String> filesInFolder = Language.getResourceFolderFiles("languages");
        if (filesInFolder.size() <= 0) {
            return false;
        }

        for (var languageFile : filesInFolder) {
            ConfigAdapter config = create(new File(languageFile));
            String langCode = config.getString("language_code");

            languageFiles.put(langCode, config);
            if (langCode.equalsIgnoreCase(Language.FALLBACK_LANGUAGE)) {
                fallbackStorage = new Storage(config, langCode);
            }
        }

        final HashMap<String, Storage> availableLanguages = new HashMap<>();
        for (var key : languageFiles.keySet()) {
            ConfigAdapter config = languageFiles.get(key);
            Storage storage = new Storage(config, key, fallbackStorage);

            availableLanguages.put(key, storage);
        }

        Language.getInstance().setAvailableLanguages(new HashMap<>(availableLanguages));
        return true;
    }

    private ConfigAdapter create(File file) {
        if (Language.isSpigot()) {
            return SpigotConfigAdapter.create(file);
        } else {
            return BungeeConfigAdapter.create(file);
        }
    }

}
