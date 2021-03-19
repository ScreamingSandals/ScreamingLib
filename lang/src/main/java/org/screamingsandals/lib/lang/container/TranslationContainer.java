package org.screamingsandals.lib.lang.container;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collection;
import java.util.List;

public interface TranslationContainer {

    /**
     * Empty translation container
     *
     * @return Empty translation container
     */
    static TranslationContainer empty() {
        return TranslationContainerImpl.EMPTY;
    }

    /**
     * Creates new translation container.
     *
     * @param node     a {@link ConfigurationNode} to translate
     * @param fallback fallback
     * @return new container
     */
    static TranslationContainer of(ConfigurationNode node, @Nullable TranslationContainer fallback) {
        return new TranslationContainerImpl(node, fallback);
    }

    /**
     * Creates new translation container.
     *
     * @param node a {@link ConfigurationNode} to translate
     * @return new container with EMPTY fallback
     */
    static TranslationContainer of(ConfigurationNode node) {
        return new TranslationContainerImpl(node, empty());
    }

    /**
     * @return root translation node
     */
    ConfigurationNode getNode();

    /**
     * Replaces the node for translating
     *
     * @param node node
     */
    void setNode(ConfigurationNode node);

    /**
     * @return fallback container
     */
    TranslationContainer getFallbackContainer();

    /**
     * replaces current fallback container
     *
     * @param fallbackContainer new fallback
     */
    void setFallbackContainer(TranslationContainer fallbackContainer);

    /**
     * @param key key to translate
     * @return translated list of strings
     */
    List<String> translate(Collection<String> key);

    /**
     * @param key key to translate
     * @return translated list of strings
     */
    List<String> translate(String... key);
}
