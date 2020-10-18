package org.screamingsandals.lib.core.lang;

import com.google.inject.Inject;
import lombok.Data;
import org.screamingsandals.lib.core.PluginCore;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;
import org.screamingsandals.lib.core.lang.registry.LanguageRegistry;
import org.screamingsandals.lib.core.lang.registry.PlayerRegistry;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.io.File;


@Data
public class LanguageBase {
    public static String FALLBACK_LANGUAGE = "en";
    private static LanguageBase instance;

    private final PluginCore pluginCore;
    private final FileRegistry fileRegistry;
    private final LanguageRegistry languageRegistry;
    private final PlayerRegistry playerRegistry;

    private String defaultLanguage = "en";
    private LanguageContainer defaultContainer;
    private String customPrefix;
    private File customDataFolder;

    @Inject
    public LanguageBase(PluginCore pluginCore, FileRegistry fileRegistry,
                        LanguageRegistry languageRegistry, PlayerRegistry playerRegistry) {
        this.pluginCore = pluginCore;
        this.fileRegistry = fileRegistry;
        this.languageRegistry = languageRegistry;
        this.playerRegistry = playerRegistry;

        final var container = languageRegistry.get(defaultLanguage);

        if (container.isEmpty()) {
            throw new UnsupportedOperationException("Default container not found!");
        }

        this.defaultContainer = container.get();
    }

    public void reload() {

    }


    public static LanguageBase getInstance() {
        return instance;
    }

    public static LanguageContainer getDefaultContainer() {
        return instance.defaultContainer;
    }
}
