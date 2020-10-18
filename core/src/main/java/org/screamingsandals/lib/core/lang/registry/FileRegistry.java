package org.screamingsandals.lib.core.lang.registry;

import lombok.Data;
import org.screamingsandals.lib.core.PluginCore;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author ScreamingSandals team
 * <p>
 * customConfigs == files from customFolder
 * originalConfigs == files form classpath
 */
@Data
public class FileRegistry {
    private final Map<String, SConfig> customConfigs = new HashMap<>();
    private final Map<String, SConfig> originalConfigs = new HashMap<>();

    private final PluginCore plugin;
    private final LanguageRegistry registry;
    private final Logger log;

    public FileRegistry(PluginCore plugin, LanguageRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
        this.log = plugin.getLog();

        registerFromClasspath();
    }

    /**
     * Registers custom languages
     *
     * @param file custom data folder to register from
     */
    public void registerCustomLanguages(File file) {

    }

    public void registerLanguage(SConfig sConfig) {
        final var code = sConfig.get("language_code").getString();

        customConfigs.putIfAbsent(code, sConfig);
        registry.register(code, new LanguageContainer(sConfig,
                registry.getOriginal(code)
                        .orElse(registry.getOriginal(LanguageBase.FALLBACK_LANGUAGE)
                                .orElse(null)), code));
    }

    public void removeLanguage(String code) {
        registry.remove(code);
    }

    private void registerFromClasspath() {
        getFilesFromClasspath().forEach(path -> {
            try {
                final var sConfig = SConfig.create(SConfig.Format.HOCON, path);
                final var code = sConfig.get("language_code").getString();

                originalConfigs.put(code, sConfig);
                registry.registerInternal(code, new LanguageContainer(sConfig, null, code));
            } catch (Exception e) {
                log.warn("Registering of internal language file failed!", e);
            }
        });
    }

    private List<Path> getFilesFromClasspath() {
        final List<Path> toReturn = new LinkedList<>();

        final var codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            return toReturn;
        }

        final var url = codeSource.getLocation();
        try (final var zipInputStream = new ZipInputStream(url.openStream())) {
            while (true) {
                final var zipEntry = zipInputStream.getNextEntry();

                if (zipEntry == null) {
                    break;
                }

                final var entryName = zipEntry.getName();
                if (entryName.startsWith("languages") && entryName.endsWith(".conf")) {
                    final var classpathResource = "/" + entryName;

                    toReturn.add(Paths.get(ClassLoader.getSystemResource(classpathResource).toURI()));
                }
            }
        } catch (Exception e) {
            log.warn("Exception occurred while loading language file!", e);
        }

        return toReturn;
    }
}
