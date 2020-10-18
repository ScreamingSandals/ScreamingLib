package org.screamingsandals.lib.core.lang;

import com.google.inject.Inject;
import lombok.Data;
import org.screamingsandals.lib.core.lang.storage.FileStorage;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Base {
    private static Base instance;
    public static String FALLBACK_LANGUAGE = "en";
    private LanguageContainer globalContainer;

    private Object plugin;
    private String customPrefix;
    private String globalLanguage;
    private File customDataFolder;
    private FileStorage fileStorage;
    private Map<String, Storage> availableLanguages = new HashMap<>();

    @Inject
    public Base(Object plugin) {
        this(plugin, null, null, "");
    }

    public Base(Object plugin, String globalLanguage) {
        this(plugin, globalLanguage, null, "");
    }

    public Base(Object plugin, String globalLanguage, String customPrefix) {
        this(plugin, globalLanguage, null, customPrefix);
    }

    public Base(Object plugin, File customDataFolder) {
        this(plugin, null, customDataFolder, "");
    }

    public Base(Object plugin, String globalLanguage, File customDataFolder, String customPrefix) {
        instance = this;
        this.plugin = plugin;
        this.globalLanguage = globalLanguage;
        this.customDataFolder = customDataFolder;
        this.customPrefix = customPrefix;

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

        globalContainer = getStorage(globalLanguage);

        Utils.sendPluginMessage(Utils.colorize("&aSuccessfully loaded messages for plugin &e%pluginName%&a!"));
        Utils.sendPluginMessage(Utils.colorize("&aSelected language: &e" + (globalContainer != null ? globalContainer.getLanguageName() : FALLBACK_LANGUAGE + " - &cFALLBACK!")));
    }

    public static Base getInstance() {
        return instance;
    }

    public static LanguageContainer getGlobalContainer() {
        return instance.globalContainer;
    }
}
