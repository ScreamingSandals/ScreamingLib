package org.screamingsandals.lib.lang.container;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collection;
import java.util.List;

public interface TranslationContainer {

    static TranslationContainer empty() {
        return TranslationContainerImpl.EMPTY;
    }

    static TranslationContainer of(ConfigurationNode node, @Nullable TranslationContainer fallback) {
        return new TranslationContainerImpl(node, fallback);
    }

    static TranslationContainer of(ConfigurationNode node) {
        return new TranslationContainerImpl(node, null);
    }

    List<String> translate(Collection<String> key);

    List<String> translate(String... key);
}
