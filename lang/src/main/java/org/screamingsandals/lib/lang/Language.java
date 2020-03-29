package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.lang.files.FileStorage;
import org.screamingsandals.lib.lang.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author ScreamingSandals team
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Language extends I {
    @Getter
    private static Language instance;
    private final Object plugin;
    private String globalLanguage;
    private File customDataFolder;
    private FileStorage fileStorage;
    private HashMap<String, Storage> availableLanguages = new HashMap<>();

    public Language(Object plugin) {
        this(plugin, null, null);
    }

    public Language(Object plugin, String globalLanguage) {
        this(plugin, globalLanguage, null);
    }

    public Language(Object plugin, File customDataFolder) {
        this(plugin, null, customDataFolder);
    }

    public Language(Object plugin, String globalLanguage, File customDataFolder) {
        instance = this;
        this.plugin = plugin;
        this.globalLanguage = globalLanguage;
        this.customDataFolder = customDataFolder;

        initializeFileStorage();
    }

    public static Storage getStorage(String langCode) {
        for (String code : instance.availableLanguages.keySet()) {
            if (code.equalsIgnoreCase(langCode)) {
                return instance.availableLanguages.get(code);
            }
        }
        return null;
    }

    public static List<String> getResourceFolderFiles(String folder) {
        List<String> toReturn = new ArrayList<>();
        try {
            CodeSource codeSource = instance.plugin.getClass().getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                URL url = codeSource.getLocation();
                ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
                while (true) {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) {
                        break;
                    }
                    String entryName = zipEntry.getName();
                    if (entryName.startsWith(folder) && entryName.endsWith(".yml")) {
                        toReturn.add(entryName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean isSpigot() {
        try {
            Class.forName("org.bukkit.plugin.Plugin");
            return true;
        } catch (ClassNotFoundException e1) {
            return false;
        }
    }

    private void initializeFileStorage() {
        fileStorage = new FileStorage(plugin);

        if (!fileStorage.load()) {
            Utils.sendPluginMessage(Utils.colorize("&cNo language file was found for plugin &e%pluginName%&c!"));
            return;
        }
        globalStorage = getStorage(globalLanguage);

        Utils.sendPluginMessage(Utils.colorize("&aSuccessfully loaded messages for plugin &e%pluginName%&a!"));
        Utils.sendPluginMessage(Utils.colorize("&aSelected language: &e" + (globalStorage != null ? globalStorage.getLanguageName() : FALLBACK_LANGUAGE + " - &cFALLBACK!")));
    }
}
