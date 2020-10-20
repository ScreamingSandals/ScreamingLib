package org.screamingsandals.lib.core.lang.registry;

import com.google.inject.Inject;
import lombok.Data;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.screamingsandals.lib.core.papi.PlaceholderConfig;
import org.screamingsandals.lib.core.wrapper.PluginWrapper;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author ScreamingSandals team
 * <p>
 * customConfigs == files from customFolder
 * originalConfigs == files form classpath
 */
@Data
public class FileRegistry {
    private final PluginWrapper pluginWrapper;
    private final LanguageRegistry registry;
    private final PlaceholderConfig papiConfig;
    private final Logger log;
    private String customPrefix;

    @Inject
    public FileRegistry(PluginWrapper pluginWrapper, LanguageRegistry registry, PlaceholderConfig papiConfig) {
        this.pluginWrapper = pluginWrapper;
        this.registry = registry;
        this.papiConfig = papiConfig;
        this.log = pluginWrapper.getLog();

        registerFromClasspath();
    }

    /**
     * Registers custom languages
     *
     * @param file custom data folder to register from
     */
    public void registerCustomLanguages(File file) {
        final var folderContext = file.listFiles();

        for (File langFile : folderContext) {
            if (langFile.getName().endsWith(".conf")) {
                try {
                    registerLanguage(SConfig.create(SConfig.Format.HOCON, langFile.toPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerLanguage(SConfig sConfig) {
        if (sConfig.node("language_code").empty()) {
            log.warn("Cannot register language!");
            return;
        }

        final var code = sConfig.node("language_code").getString();

        log.debug("Registering new language with code {}", code);

        registry.register(code, new LanguageContainer(sConfig,
                registry.getOriginal(code)
                        .orElse(registry.getOriginal(LanguageBase.FALLBACK_LANGUAGE)
                                .orElse(null)),
                code, customPrefix, papiConfig, pluginWrapper));
    }

    public void removeLanguage(String code) {
        registry.remove(code);
    }

    public void registerFromClasspath() {
        getFilesFromClasspath().forEach(path -> {
            try {
                final var sConfig = SConfig.create(SConfig.Format.HOCON, path);
                final var code = sConfig.node("language_code").getString();

                registry.registerInternal(code,
                        new LanguageContainer(sConfig, null, code, customPrefix, papiConfig, pluginWrapper));
            } catch (Exception e) {
                log.warn("Registering of internal language file failed!", e);
            }
        });
    }

    public List<Path> getFilesFromClasspath() {
        final List<Path> toReturn = new LinkedList<>();

        final var codeSource = pluginWrapper.getClass().getProtectionDomain().getCodeSource();
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
                    final var resource = pluginWrapper.getPlugin().getClass().getResource("/" + entryName).toURI();

                    //cool hack from StackOverflow :)
                    if ("jar".equals(resource.getScheme())) {
                        for (var provider : FileSystemProvider.installedProviders()) {
                            if (provider.getScheme().equalsIgnoreCase("jar")) {
                                try {
                                    provider.getFileSystem(resource);
                                } catch (FileSystemNotFoundException e) {
                                    provider.newFileSystem(resource, Collections.emptyMap());
                                }
                            }
                        }
                    }

                    toReturn.add(Paths.get(resource));
                }
            }
        } catch (Exception e) {
            log.warn("Exception occurred while loading language file!", e);
        }

        return toReturn;
    }
}
