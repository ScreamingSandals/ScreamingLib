package org.screamingsandals.lib.core.lang.storage;

import lombok.Data;
import org.screamingsandals.lib.core.PluginCore;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.lang.LanguageBase;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author ScreamingSandals team
 */
@Data
public class FileStorage {
    private final PluginCore plugin;
    private final Map<String, SConfig> languageConfig = new HashMap<>();
    private final Map<String, LanguageContainer> registeredLanguages = new HashMap<>();

    public FileStorage(PluginCore plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        getFiles().forEach(this::registerLanguage);

        languageConfig.forEach((code, config) -> {
            final var container = new LanguageContainer(config, registeredLanguages.getOrDefault(LanguageBase.FALLBACK_LANGUAGE, null), code);
            registeredLanguages.put(code, container);
        });

        return !registeredLanguages.isEmpty();
    }

    private void registerLanguage(String fileName) {
        final var dataFolder = plugin.getPluginFolder();
        final var classpathResource = "/" + fileName;
        SConfig sConfig;

        try {
            if (dataFolder != null && dataFolder.exists()) {
                final File langFile = new File(dataFolder, fileName);
                if (langFile.exists()) {
                    sConfig = SConfig.create(SConfig.Format.HOCON, langFile.toPath());

                    final var key = sConfig.get("language_code").getString();
                    languageConfig.put(key, sConfig);
                }
            }

            sConfig = SConfig.create(SConfig.Format.HOCON, Paths.get(ClassLoader.getSystemResource(classpathResource).toURI()));

            final var key = sConfig.get("language_code").getString();
            languageConfig.put(key, sConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getFiles() {
        final List<String> toReturn = new ArrayList<>();
        try {
            final CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                final URL url = codeSource.getLocation();
                final ZipInputStream zipInputStream = new ZipInputStream(url.openStream());

                while (true) {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) {
                        break;
                    }
                    String entryName = zipEntry.getName();
                    if (entryName.startsWith("languages") && entryName.endsWith(".conf")) {
                        toReturn.add(entryName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
