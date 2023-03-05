/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.spectator.Component;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that provides containers ({@link TranslationContainer}) for given locales.
 */
public abstract class LangService {
    protected final @NotNull Map<@NotNull Locale, TranslationContainer> containers = new ConcurrentHashMap<>();
    @Setter
    @Getter
    protected @NotNull TranslationContainer fallbackContainer = TranslationContainer.empty();

    /**
     * Resolves a {@link TranslationContainer} for given Locale.
     *
     * @param locale locale to resolve
     * @return a {@link TranslationContainer} or null if there is no container for the given locale or if the locale is null
     */
    @Contract("null -> null")
    public @Nullable TranslationContainer getFor(@Nullable Locale locale) {
        return containers.get(locale);
    }

    /**
     * Resolves a {@link TranslationContainer} for given {@link CommandSender}.
     *
     * @param sender sender from who to resolve
     * @return a {@link TranslationContainer}
     */
    public @NotNull TranslationContainer getFor(@Nullable CommandSender sender) {
        /* SINGLE LANGUAGE */
        if (containers.isEmpty() || sender == null) {
            return fallbackContainer;
        }

        /* MULTI LANGUAGE */
        final var senderLocale = resolveLocale(sender);
        final var container = containers.get(senderLocale);
        if (container != null) {
            return container;
        }

        return containers
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getLanguage().equals(senderLocale.getLanguage()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(fallbackContainer);
    }

    /**
     * @return a default prefix.
     */
    public @NotNull Component getDefaultPrefix() {
        return Lang.getDefaultPrefix();
    }

    /**
     * Tries to resolve prefix from given {@link CommandSender}.
     * <p>
     * NOTE: default implementation is only getting {@link Lang#getDefaultPrefix()}.
     * This is here for abstraction.
     *
     * @param sender from whom to resolve locale
     * @return resolved prefix
     */
    public @NotNull Component resolvePrefix(@Nullable CommandSender sender) {
        return Lang.getDefaultPrefix();
    }

    /**
     * Tries to resolve {@link Locale} from given {@link CommandSender}.
     * <p>
     * NOTE: default implementation is only getting {@link CommandSender#getLocale()}.
     * This is here for abstraction.
     *
     * @param sender from whom to resolve locale
     * @return resolved {@link Locale}
     */
    protected @NotNull Locale resolveLocale(@NotNull CommandSender sender) {
        return sender.getLocale();
    }

    /**
     * MessagePlaceholder is the placeholder for including another translated messages inside messages constructed by {@link Message}.
     * <p>
     * Don't be confused with \<lang>, that is placeholder for including translates from resource pack or vanilla translate.
     * <p>
     * Message placeholder is only supported with MiniMessage format!
     * Other placeholders are automatically propagated.
     *
     * @return the message placeholder name or null
     */
    public @Nullable String getMessagePlaceholderName() {
        return null;
    }
}
