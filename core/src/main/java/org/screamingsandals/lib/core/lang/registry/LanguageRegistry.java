package org.screamingsandals.lib.core.lang.registry;

import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LanguageRegistry {
    private final Map<String, LanguageContainer> registeredContainers = new HashMap<>();

    public void register(String languageCode, LanguageContainer languageContainer) {
        registeredContainers.putIfAbsent(languageCode, languageContainer);
    }

    public void remove(String languageCode) {
        registeredContainers.remove(languageCode);
    }

    public void clear() {
        registeredContainers.clear();
    }

    public Optional<LanguageContainer> getByCode(String languageCode) {
        return Optional.ofNullable(registeredContainers.get(languageCode));
    }
}
