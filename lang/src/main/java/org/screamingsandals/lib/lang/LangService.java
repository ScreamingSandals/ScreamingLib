/*
 * Copyright 2022 ScreamingSandals
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
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that provides containers ({@link TranslationContainer}) for given locales.
 */
public abstract class LangService {
    protected final Map<Locale, TranslationContainer> containers = new ConcurrentHashMap<>();
    @Setter
    @Getter
    protected TranslationContainer fallbackContainer = TranslationContainer.empty();

    /**
     * Resolves a {@link TranslationContainer} for given Locale.
     *
     * @param locale locale to resolve
     * @return a {@link TranslationContainer} wrapped in {@link Optional}
     */
    public Optional<TranslationContainer> getFor(Locale locale) {
        return Optional.ofNullable(containers.get(locale));
    }

    /**
     * Resolves a {@link TranslationContainer} for given {@link CommandSenderWrapper}.
     *
     * @param sender sender from who to resolve
     * @return a {@link TranslationContainer}
     */
    public TranslationContainer getFor(CommandSenderWrapper sender) {
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
    public Component getDefaultPrefix() {
        return Lang.getDefaultPrefix();
    }

    /**
     * Tries to resolve prefix from given {@link CommandSenderWrapper}.
     * <p>
     * NOTE: default implementation is only getting {@link Lang#getDefaultPrefix()}.
     * This is here for abstraction.
     *
     * @param sender from whom to resolve locale
     * @return resolved prefix
     */
    public @NotNull Component resolvePrefix(@NotNull CommandSenderWrapper sender) {
        return Lang.getDefaultPrefix();
    }

    /**
     * Tries to resolve {@link Locale} from given {@link CommandSenderWrapper}.
     * <p>
     * NOTE: default implementation is only getting {@link CommandSenderWrapper#getLocale()}.
     * This is here for abstraction.
     *
     * @param sender from whom to resolve locale
     * @return resolved {@link Locale}
     */
    protected @NotNull Locale resolveLocale(@NotNull CommandSenderWrapper sender) {
        return sender.getLocale();
    }
}
