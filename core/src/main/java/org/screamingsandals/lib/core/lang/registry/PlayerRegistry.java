package org.screamingsandals.lib.core.lang.registry;

import com.google.inject.Inject;
import lombok.Data;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
public class PlayerRegistry {
    private final Map<UUID, String> players = new HashMap<>();
    private final LanguageRegistry languageRegistry;

    @Inject
    public PlayerRegistry(LanguageRegistry languageRegistry) {
        this.languageRegistry = languageRegistry;

        final var consoleID = UUID.randomUUID();
        System.setProperty("slang.consoleUUID", consoleID.toString());

        register(consoleID, "cz"); //TODO
    }

    public void register(UUID uuid, String languageCode) {
        players.putIfAbsent(uuid, languageCode);
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

    public String getLanguageCodeFor(UUID uuid) {
        final var code = players.get(uuid);

        if (code == null) {
            return LanguageBase.getDefaultLanguage();
        }

        return code;
    }

    public Optional<LanguageContainer> getFor(UUID uuid) {
        return languageRegistry.get(getLanguageCodeFor(uuid));
    }
}
