package org.screamingsandals.lib.core.lang.registry;

import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LanguageRegistry {
    private final Map<String, LanguageContainer> customContainers = new HashMap<>();
    private final Map<String, LanguageContainer> originalContainers = new HashMap<>();

    void registerInternal(String code, LanguageContainer container) {
        originalContainers.put(code, container);
    }

    void removeInternal(String code) {
        originalContainers.remove(code);
    }

    public Optional<LanguageContainer> getOriginal(String code) {
        return Optional.ofNullable(originalContainers.get(code));
    }

    public void register(String code, LanguageContainer container) {
        customContainers.putIfAbsent(code, container);
    }

    public void remove(String code) {
        customContainers.remove(code);
    }

    public void clear() {
        originalContainers.clear();
        customContainers.clear();
    }

    public Optional<LanguageContainer> getCustom(String code) {
        return Optional.ofNullable(customContainers.get(code));
    }

    public Optional<LanguageContainer> get(String code) {
        if (customContainers.containsKey(code)) {
            return Optional.of(customContainers.get(code));
        }

        return Optional.ofNullable(originalContainers.get(code));
    }

    public Optional<LanguageContainer> getFirstAvailable() {
        if (customContainers.isEmpty()) {
            return originalContainers.values().stream().findFirst();
        }

        return customContainers.values().stream().findFirst();
    }
}
