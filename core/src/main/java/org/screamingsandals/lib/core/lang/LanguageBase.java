package org.screamingsandals.lib.core.lang;

import lombok.Data;
import org.screamingsandals.lib.core.PluginCore;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.io.File;

@Data
public abstract class LanguageBase {
    public static String FALLBACK_LANGUAGE = "en";
    private static LanguageBase instance;

    private String defaultLanguage;
    private LanguageContainer defaultContainer;

    private PluginCore plugin;
    private String customPrefix;
    private File customDataFolder;

    public LanguageBase(PluginCore plugin) {
        this(plugin, null, null, "");
    }

    public LanguageBase(PluginCore plugin, String defaultLanguage) {
        this(plugin, defaultLanguage, null, "");
    }

    public LanguageBase(PluginCore plugin, String defaultLanguage, String customPrefix) {
        this(plugin, defaultLanguage, null, customPrefix);
    }

    public LanguageBase(PluginCore plugin, File customDataFolder) {
        this(plugin, null, customDataFolder, "");
    }

    public LanguageBase(PluginCore plugin, String defaultLanguage, File customDataFolder, String customPrefix) {
        instance = this;
        this.plugin = plugin;
        this.defaultLanguage = defaultLanguage;
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
        fileStorage = new FileRegistry(plugin);

        if (!fileStorage.load()) {
            Utils.sendPluginMessage(Utils.colorize("&cNo language file was found for plugin &e%pluginName%&c!"));
            return;
        }

        defaultContainer = getStorage(defaultLanguage);

        Utils.sendPluginMessage(Utils.colorize("&aSuccessfully loaded messages for plugin &e%pluginName%&a!"));
        Utils.sendPluginMessage(Utils.colorize("&aSelected language: &e" + (defaultContainer != null ? defaultContainer.getLanguageName() : FALLBACK_LANGUAGE + " - &cFALLBACK!")));
    }

    public static LanguageBase getInstance() {
        return instance;
    }

    public static LanguageContainer getDefaultContainer() {
        return instance.defaultContainer;
    }
}
