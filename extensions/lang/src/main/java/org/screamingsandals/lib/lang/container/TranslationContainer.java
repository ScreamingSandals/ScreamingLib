/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.lang.container;

import org.jetbrains.annotations.NotNull;
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
    static @NotNull TranslationContainer empty() {
        return TranslationContainerImpl.EMPTY;
    }

    /**
     * Creates new translation container.
     *
     * @param node     a {@link ConfigurationNode} to translate
     * @param fallback fallback
     * @return new container
     */
    static @NotNull TranslationContainer of(@NotNull ConfigurationNode node, @Nullable TranslationContainer fallback) {
        return new TranslationContainerImpl(node, fallback);
    }

    /**
     * Creates new translation container.
     *
     * @param node a {@link ConfigurationNode} to translate
     * @return new container with EMPTY fallback
     */
    static @NotNull TranslationContainer of(@NotNull ConfigurationNode node) {
        return new TranslationContainerImpl(node, empty());
    }

    /**
     * @return root translation node
     */
    @NotNull ConfigurationNode getNode();

    /**
     * Replaces the node for translating
     *
     * @param node node
     */
    void setNode(@NotNull ConfigurationNode node);

    /**
     * @return fallback container
     */
    @Nullable TranslationContainer getFallbackContainer();

    /**
     * replaces current fallback container
     *
     * @param fallbackContainer new fallback
     */
    void setFallbackContainer(@Nullable TranslationContainer fallbackContainer);

    /**
     * @param key key to translate
     * @return translated list of strings
     */
    @NotNull List<@NotNull String> translate(@NotNull Collection<@NotNull String> key);

    /**
     * @param key key to translate
     * @return translated list of strings
     */
    @NotNull List<@NotNull String> translate(@NotNull String @NotNull... key);

    /**
     * Checks if this container is empty.
     * @return true if empty
     */
    boolean isEmpty();
}
