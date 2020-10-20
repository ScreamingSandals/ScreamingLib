package org.screamingsandals.lib.core.lang;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;
import org.screamingsandals.lib.core.lang.registry.LanguageRegistry;
import org.screamingsandals.lib.core.lang.registry.PlayerRegistry;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.screamingsandals.lib.core.wrapper.PluginWrapper;

import java.io.File;

/**
 * LanguageBase, might change this..
 * Right now, this is used for configuring language itself.
 */
@Getter
@Setter
public class LanguageBase {
    public static String FALLBACK_LANGUAGE = "en";
    private static LanguageBase instance;

    private final PluginWrapper pluginWrapper;
    private final FileRegistry fileRegistry;
    private final LanguageRegistry languageRegistry;
    private final PlayerRegistry playerRegistry;

    private String defaultLanguage = "en";
    private LanguageContainer defaultContainer;
    private String customPrefix;
    private File customDataFolder;

    @Inject
    public LanguageBase(PluginWrapper pluginWrapper, FileRegistry fileRegistry,
                        LanguageRegistry languageRegistry, PlayerRegistry playerRegistry) {
        instance = this;
        this.pluginWrapper = pluginWrapper;
        this.fileRegistry = fileRegistry;
        this.languageRegistry = languageRegistry;
        this.playerRegistry = playerRegistry;
        this.defaultContainer = getDefaultOrFirstAvailableContainer();
    }

    public void reload() {
        languageRegistry.clear();

        fileRegistry.setCustomPrefix(customPrefix);
        fileRegistry.registerFromClasspath();

        if (customDataFolder != null) {
            fileRegistry.registerCustomLanguages(customDataFolder);
        }
    }

    public static LanguageContainer getDefaultContainer() {
        return instance.defaultContainer;
    }

    public static String getDefaultLanguage() {
        return instance.defaultLanguage;
    }

    public static PlayerRegistry getPlayerRegistry() {
        return instance.playerRegistry;
    }

    public static PluginWrapper getPluginWrapper() {
        return instance.pluginWrapper;
    }

    private LanguageContainer getDefaultOrFirstAvailableContainer() {
        final var container = languageRegistry.get(defaultLanguage);

        if (container.isEmpty()) {
            final var firstAvailable = languageRegistry.getFirstAvailable();
            if (firstAvailable.isEmpty()) {
                throw new UnsupportedOperationException("No language container found!");
            }
            return firstAvailable.get();
        }
        return container.get();
    }
}
