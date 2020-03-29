package org.screamingsandals.lib.lang.files;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.lang.Language;
import org.screamingsandals.lib.lang.files.config.ConfigAdapter;
import org.screamingsandals.lib.lang.storage.Storage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

        for (String file : filesInFolder) {
            ConfigAdapter config = ConfigAdapter.create(
                    new InputStreamReader(getResourceAsStream(file), StandardCharsets.UTF_8));
            String langCode = config.getString("language_code");

            languageFiles.put(langCode, config);
            if (langCode.equalsIgnoreCase(Language.FALLBACK_LANGUAGE)) {
                fallbackStorage = new Storage(config, langCode);
            }
        }

        HashMap<String, Storage> availableLanguages = new HashMap<>();
        for (String key : languageFiles.keySet()) {
            ConfigAdapter config = languageFiles.get(key);
            Storage storage = new Storage(config, key, fallbackStorage);

            availableLanguages.put(key, storage);
        }

        Language.getInstance().setAvailableLanguages(new HashMap<>(availableLanguages));
        return true;
    }

    private InputStream getResourceAsStream(String resource) {
        if (Language.isSpigot()) {
            Plugin plugin = (Plugin) this.plugin;
            return plugin.getResource(resource);
        } else {
            net.md_5.bungee.api.plugin.Plugin plugin = (net.md_5.bungee.api.plugin.Plugin) this.plugin;
            return plugin.getResourceAsStream(resource);
        }
    }

}
