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

package org.screamingsandals.lib.lang;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.placeholders.PlaceholderManager;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Translated message.
 */
@Data
public class Message implements TitleableAudienceComponentLike, Cloneable {
    private static final @NotNull Pattern LEGACY_PLACEHOLDERS = Pattern.compile("[%]([^%]+)[%]");
    private static final @NotNull Pattern EARLY_MINI_MESSAGE_PLACEHOLDERS = Pattern.compile("[<]([^>]+)[>]");

    private final @NotNull List<@NotNull Messageable> translations = new LinkedList<>();
    private final @NotNull List<@NotNull Function<@Nullable CommandSender, @NotNull Placeholder>> placeholders = new ArrayList<>();
    private final @NotNull Map<@NotNull String, @NotNull String> earlyPlaceholders = new HashMap<>();
    @Accessors(chain = true)
    @Setter(onMethod_ = @ApiStatus.Internal)
    @Getter(onMethod_ = @ApiStatus.Internal)
    private Placeholder @Nullable [] rawPlaceholders;
    private final @NotNull LangService langService;
    private @NotNull Component prefix;
    private @Nullable TimesProvider times;
    private @NotNull PrefixPolicy prefixPolicy = PrefixPolicy.ALL_MESSAGES;
    private @NotNull PrefixResolving prefixResolving = PrefixResolving.DEFAULT;
    @Accessors(chain = true, fluent = true)
    private @NotNull LinkPolicy linkPolicy = LinkPolicy.ONLY_NON_TRANSLATIONS;

    public <M extends Messageable> Message(@NotNull Collection<@NotNull M> translations, @NotNull LangService langService, @NotNull Component prefix) {
        this.translations.addAll(translations);
        this.langService = langService;
        this.prefix = prefix;
    }

    /**
     * Creates new empty Message.
     *
     * @return empty message.
     */
    public static @NotNull Message empty() {
        return new Message(List.of(), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new empty Message.
     *
     * @param prefix custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull Component prefix) {
        return new Message(List.of(), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new empty Message.
     *
     * @param prefix custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull ComponentLike prefix) {
        return new Message(List.of(), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new empty Message.
     *
     * @param service custom {@link LangService} to use.
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull LangService service) {
        return new Message(List.of(), service, Component.empty());
    }

    /**
     * Creates new empty Message.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull LangService service, @NotNull Component prefix) {
        return new Message(List.of(), service, prefix);
    }

    /**
     * Creates new empty Message.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull LangService service, @NotNull ComponentLike prefix) {
        return new Message(List.of(), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), service, prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull Component prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull ComponentLike prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull Component prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull ComponentLike prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull Component prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull ComponentLike prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull String... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull Component prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), service, prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message, Messageable.Type.ADVENTURE)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull Component prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), service, prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String @NotNull... messages) {
        return new Message(List.of(StringMessageable.of(Messageable.Type.ADVENTURE, messages)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull Component prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), service, prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service  custom {@link LangService} to use.
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull List<@NotNull String> messages) {
        return new Message(List.of(StringMessageable.of(messages, Messageable.Type.ADVENTURE)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull Component prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull ComponentLike prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull Component prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), service, prefix);
    }

    /**
     * Creates new {@link Message} from a text using MiniMessage format.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofRichText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Supplier<@NotNull List<@NotNull String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message, Messageable.Type.ADVENTURE)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param key key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Translation}
     *
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Translation translation) {
        return new Message(List.of(translation), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull List<@NotNull M> translations) {
        return new Message(translations, Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service custom {@link LangService} to use.
     * @param key     key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service     custom {@link LangService} to use.
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Translation translation) {
        return new Message(List.of(translation), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service      custom {@link LangService} to use.
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull List<@NotNull M> translations) {
        return new Message(translations, service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix custom prefix to use
     * @param key    key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix custom prefix to use
     * @param key    key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix      custom prefix to use
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix      custom prefix to use
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull Component prefix, @NotNull List<@NotNull M> translations) {
        return new Message(translations, Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull ComponentLike prefix, @NotNull List<@NotNull M> translations) {
        return new Message(translations, Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }


    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), service, prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String @NotNull... key) {
        return new Message(List.of(Translation.of(key)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service     custom {@link LangService} to use.
     * @param prefix      custom prefix to use
     * @param translation translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), service, prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service     custom {@link LangService} to use.
     * @param prefix      custom prefix to use
     * @param translation translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service      custom {@link LangService} to use.
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull List<@NotNull M> translations) {
        return new Message(translations, service, prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service      custom {@link LangService} to use.
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull List<@NotNull M> translations) {
        return new Message(translations, service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param key translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service custom {@link LangService} to use.
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix custom prefix to use
     * @param key    translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix custom prefix to use
     * @param key    translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), Objects.requireNonNull(Lang.getDefaultService()), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), service, prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Collection<@NotNull String> key) {
        return new Message(List.of(Translation.of(key)), service, prefix.asComponent());
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, byte value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, short value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, int value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull IntSupplier value) {
        return placeholder(Placeholder.lazyNumber(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, long value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull LongSupplier value) {
        return placeholder(Placeholder.lazyNumber(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, char value) {
        return placeholder(Placeholder.character(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, boolean value) {
        return placeholder(Placeholder.bool(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull BooleanSupplier value) {
        return placeholder(Placeholder.lazyBool(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, double value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull DoubleSupplier value) {
        return placeholder(Placeholder.lazyNumber(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, float value) {
        return placeholder(Placeholder.number(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @param round       how many decimal points should the number have
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, double value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(Placeholder.number(placeholder, (double) Math.round(value * pow) / pow));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @param round       how many decimal points should the number have
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, float value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(Placeholder.number(placeholder, (double) Math.round(value * pow) / pow));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param accessor placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull TemporalAccessor accessor) {
        return placeholder(Placeholder.dateTime(placeholder, accessor));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value in the MiniMessage format
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull String value) {
        return placeholder(Placeholder.parsed(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholderRaw(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull String value) {
        return placeholder(Placeholder.unparsed(placeholder, value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param component   placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull Component component) {
        return placeholder(Placeholder.component(placeholder, component));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param component   placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull ComponentLike component) {
        return placeholder(placeholder, sender -> {
            if (component instanceof AudienceComponentLike) {
                return ((AudienceComponentLike) component).asComponent(sender);
            }
            return component.asComponent();
        });
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param translation placeholder value, resolved from {@link Translation}
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull Translation translation) {
        return placeholder(placeholder, of(translation));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param message     placeholder value, resolved from {@link Message}
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull Message message) {
        return placeholder(placeholder, message::getForJoined);
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder       placeholder key
     * @param componentFunction function that returns a {@link Component} as the placeholder value.
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull @org.intellij.lang.annotations.Pattern("[a-z\\d_-]+") String placeholder, @NotNull Function<@Nullable CommandSender, @NotNull Component> componentFunction) {
        placeholders.add(sender -> Placeholder.lazyComponent(placeholder, () -> componentFunction.apply(sender)));
        return this;
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholderFunction function that returns a {@link Placeholder} for the specific {@link CommandSender}.
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull Function<@Nullable CommandSender, @NotNull Placeholder> placeholderFunction) {
        placeholders.add(placeholderFunction);
        return this;
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder for Spectator
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull Placeholder placeholder) {
        placeholders.add(sender -> placeholder);
        return this;
    }

    /**
     * This method works only with Messages using MiniMessage format.
     * It replaces the placeholder BEFORE the message is processed by MiniMessage,
     * so you can change format for whole message, not just the inserted part.
     *
     * @param placeholder Placeholder
     * @param value       Component which will replace the placeholder
     * @return self
     */
    @Deprecated
    public @NotNull Message earlyPlaceholder(@NotNull String placeholder, @NotNull Component value) {
        earlyPlaceholders.put(placeholder, Lang.MINIMESSAGE.serialize(value));
        return this;
    }

    /**
     * This method works only with Messages using MiniMessage format.
     * It replaces the placeholder BEFORE the message is processed by MiniMessage,
     * so you can change format for whole message, not just the inserted part.
     *
     * @param placeholder Placeholder
     * @param value       String which will replace the placeholder. It must be in MiniMessage format
     * @return self
     */
    @Deprecated
    public @NotNull Message earlyPlaceholder(@NotNull String placeholder, @NotNull String value) {
        earlyPlaceholders.put(placeholder, value);
        return this;
    }

    /**
     * Sets the prefix for this message.
     * NOTE:  if the parameter is null, {@link Message#noPrefix()} is used.
     *
     * @param prefix prefix to use
     * @return self
     */
    public @NotNull Message prefix(@Nullable Component prefix) {
        if (prefix == null) {
            return noPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the prefix for this message.
     * NOTE:  if the parameter is null, {@link Message#noPrefix()} is used.
     *
     * @param prefix prefix to use
     * @return self
     */
    public @NotNull Message prefix(@Nullable ComponentLike prefix) {
        if (prefix == null) {
            return noPrefix();
        }
        this.prefix = prefix.asComponent();
        return this;
    }

    /**
     * Sets the prefix for this message.
     * NOTE:  if the parameter is null, {@link Message#defaultPrefix()} is used.
     *
     * @param prefix prefix to use
     * @return self
     */
    public @NotNull Message prefixOrDefault(@Nullable Component prefix) {
        if (prefix == null || Component.empty().equals(prefix)) {
            return defaultPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the prefix for this message.
     * NOTE:  if the parameter is null, {@link Message#defaultPrefix()} is used.
     *
     * @param prefix prefix to use
     * @return self
     */
    public @NotNull Message prefixOrDefault(@Nullable ComponentLike prefix) {
        if (prefix == null || Component.empty().equals(prefix.asComponent())) {
            return defaultPrefix();
        }
        this.prefix = prefix.asComponent();
        return this;
    }

    /**
     * Resets the prefix for this message.
     *
     * @return self
     */
    public @NotNull Message noPrefix() {
        this.prefix = Component.empty();
        return this;
    }

    /**
     * Uses default prefix from {@link LangService#getDefaultPrefix()}
     *
     * @return self
     */
    public @NotNull Message defaultPrefix() {
        this.prefix = langService.getDefaultPrefix();
        return this;
    }

    /**
     * Uses the {@link PrefixResolving#PER_PLAYER} prefix resolving policy.
     * This means that the prefix is resolved per-player from {@link LangService#resolvePrefix(CommandSender)}.
     *
     * @return self
     */
    public @NotNull Message resolvePrefix() {
        this.prefixResolving = PrefixResolving.PER_PLAYER;
        return this;
    }

    /**
     * Sets new prefix policy.
     * This changes the prefix behavior accordingly:
     * - {@link PrefixPolicy#ALL_MESSAGES} - all messages will have the prefix attached
     * - {@link PrefixPolicy#FIRST_MESSAGE} - only first message will have the prefix.
     *
     * @return self
     */
    public @NotNull Message prefixPolicy(@NotNull PrefixPolicy prefixPolicy) {
        this.prefixPolicy = prefixPolicy;
        return this;
    }

    /**
     * Sets new prefix resolving policy.
     * If the policy is {@link PrefixResolving#PER_PLAYER}, prefix is resolved per-player from {@link LangService#resolvePrefix(CommandSender)}.
     *
     * @return self
     */
    public @NotNull Message prefixResolving(@NotNull PrefixResolving resolving) {
        this.prefixResolving = resolving;
        return this;
    }

    /**
     * Joins given key into this message.
     *
     * @param key key to join
     * @return self
     */
    public @NotNull Message join(@NotNull String key) {
        this.translations.add(Translation.of(key));
        return this;
    }

    /**
     * Joins given keys into this message.
     *
     * @param keys keys to join
     * @return self
     */
    public @NotNull Message join(@NotNull String @NotNull... keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    /**
     * Joins given keys into this message.
     *
     * @param keys keys to join
     * @return self
     */
    public @NotNull Message join(@NotNull Collection<@NotNull String> keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param message message to join
     * @return self
     */
    public @NotNull Message joinPlainText(@NotNull String message) {
        this.translations.add(StringMessageable.of(message));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param messages message to join
     * @return self
     */
    public @NotNull Message joinPlainText(@NotNull String @NotNull... messages) {
        this.translations.add(StringMessageable.of(messages));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinPlainText(@NotNull List<@NotNull String> messages) {
        this.translations.add(StringMessageable.of(messages));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinPlainText(@NotNull Supplier<@NotNull List<@NotNull String>> messages) {
        this.translations.add(SupplierStringMessageable.of(messages));
        return this;
    }

    /**
     * Joins given MiniMessage-formated text into this message.
     *
     * @param message message to join
     * @return self
     */
    public @NotNull Message joinRichText(@NotNull String message) {
        this.translations.add(StringMessageable.of(message, Messageable.Type.ADVENTURE));
        return this;
    }

    /**
     * Joins given MiniMessage-formated text into this message.
     *
     * @param messages message to join
     * @return self
     */
    public @NotNull Message joinRichText(@NotNull String @NotNull... messages) {
        this.translations.add(StringMessageable.of(Messageable.Type.ADVENTURE, messages));
        return this;
    }

    /**
     * Joins given MiniMessage-formated text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinRichText(@NotNull List<@NotNull String> messages) {
        this.translations.add(StringMessageable.of(messages, Messageable.Type.ADVENTURE));
        return this;
    }

    /**
     * Joins given MiniMessage-formated text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinRichText(@NotNull Supplier<@NotNull List<@NotNull String>> messages) {
        this.translations.add(SupplierStringMessageable.of(messages, Messageable.Type.ADVENTURE));
        return this;
    }

    /**
     * Joins given translation into this message.
     *
     * @param translation translation to join
     * @return self
     */
    public @NotNull Message join(@NotNull Messageable translation) {
        this.translations.add(translation);
        return this;
    }

    /**
     * Joins given translation into this message.
     *
     * @param translations translation to join
     * @param <M>          type of the {@link Messageable}
     * @return self
     */
    public <M extends Messageable> @NotNull Message join(@NotNull List<@NotNull M> translations) {
        this.translations.addAll(translations);
        return this;
    }

    /**
     * Sets the {@link TimesProvider} for this message.
     *
     * @param times times to set
     * @return self
     */
    public @NotNull Message times(@NotNull TimesProvider times) {
        this.times = times;
        return this;
    }

    /**
     * Tries to get all the {@link Component} from this message for given sender.
     *
     * @param sender sender to resolve the components for
     * @return list of components.
     */
    public @NotNull List<@NotNull Component> getFor(@Nullable CommandSender sender) {
        final var prefixSetter = new AtomicBoolean(true);
        final var container = langService.getFor(sender);

        if (prefixResolving == PrefixResolving.PER_PLAYER) {
            this.prefix = langService.resolvePrefix(sender);
        }

        return translations
                .stream()
                .map(translation -> {
                    final var list = translation
                            .translateIfNeeded(container)
                            .stream()
                            .map(s -> {
                                if (langService.getMessagePlaceholderName() != null && (linkPolicy == LinkPolicy.ALL || (linkPolicy == LinkPolicy.ONLY_NON_TRANSLATIONS && (translation instanceof StringMessageable || translation instanceof SupplierStringMessageable)))) {
                                    var trim = s.trim();
                                    if (trim.startsWith("@") && !trim.contains(" ") && !trim.contains("<") && !trim.contains(">") && !trim.contains(":")) {
                                        // this is a link
                                        trim = trim.substring(1);
                                        return Translation.of(trim.split("(?<!\\\\)\\.")).translateIfNeeded(container);
                                    }
                                }

                                return List.of(s);
                            })
                            .flatMap(Collection::stream)
                            .map(s -> {
                                if (PlaceholderManager.isInitialized()) {
                                    // Skip this code block to avoid impact of black magic on your mind
                                    //FOR REAL.
                                    var matcher = LEGACY_PLACEHOLDERS.matcher(s);

                                    var lastIndex = 0;
                                    var output = new StringBuilder();
                                    while (matcher.find()) {
                                        output.append(s, lastIndex, matcher.start());

                                        var result = PlaceholderManager.resolveString(
                                                sender instanceof MultiPlatformOfflinePlayer ? (MultiPlatformOfflinePlayer) sender : null,
                                                "%" + matcher.group(1) + "%"
                                        );

                                        if (translation.getType() == Messageable.Type.ADVENTURE) {
                                            output.append(Lang.MINIMESSAGE.serialize(Component.fromLegacy(result)));
                                        } else {
                                            output.append(result);
                                        }
                                        lastIndex = matcher.end();
                                    }
                                    if (lastIndex < s.length()) {
                                        output.append(s, lastIndex, s.length());
                                    }
                                    s = output.toString();
                                }

                                if (translation.getType() == Messageable.Type.ADVENTURE) {
                                    if (!earlyPlaceholders.isEmpty()) {
                                        var matcher = EARLY_MINI_MESSAGE_PLACEHOLDERS.matcher(s);

                                        var lastIndex = 0;
                                        var output = new StringBuilder();
                                        while (matcher.find()) {
                                            output.append(s, lastIndex, matcher.start());
                                            if (earlyPlaceholders.containsKey(matcher.group(1))) {
                                                output.append(earlyPlaceholders.get(matcher.group(1)));
                                            } else {
                                                output.append("<").append(matcher.group(1)).append(">");
                                            }

                                            lastIndex = matcher.end();
                                        }
                                        if (lastIndex < s.length()) {
                                            output.append(s, lastIndex, s.length());
                                        }
                                        s = output.toString();
                                    }

                                    var resolvedTemplates = placeholders
                                                    .stream()
                                                    .map(entry -> entry.apply(sender));
                                    var messagePlaceholder = langService.getMessagePlaceholderName();
                                    if (messagePlaceholder != null) {
                                        resolvedTemplates = Stream.concat(resolvedTemplates, Stream.of(
                                            new MessagePlaceholder(messagePlaceholder, langService, sender)
                                        ));
                                    }
                                    if (rawPlaceholders != null) {
                                        resolvedTemplates = Stream.concat(resolvedTemplates, Stream.of(rawPlaceholders));
                                    }

                                    return Lang.MINIMESSAGE.parse(s, resolvedTemplates.toArray(Placeholder[]::new));
                                } else {
                                    // Black magic again
                                    // SKIP THIS.
                                    var matcher = LEGACY_PLACEHOLDERS.matcher(s);

                                    var lastIndex = 0;
                                    var output = new StringBuilder();
                                    var resolved = placeholders.stream()
                                                    .map(f -> f.apply(sender))
                                                    .map(e -> Map.entry(e.getName(), e))
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                    while (matcher.find()) {
                                        output.append(s, lastIndex, matcher.start());
                                        if (resolved.containsKey(matcher.group(1))) {
                                            output.append(resolved.get(matcher.group(1)).getResult(Lang.MINIMESSAGE, List.of(), resolved.values().toArray(Placeholder[]::new)).build().toLegacy());
                                        } else {
                                            output.append("%").append(matcher.group(1)).append("%");
                                        }

                                        lastIndex = matcher.end();
                                    }
                                    if (lastIndex < s.length()) {
                                        output.append(s, lastIndex, s.length());
                                    }
                                    return Component.fromLegacy(output.toString());
                                }
                            })
                            .map(component -> {
                                if (!Component.empty().equals(prefix)
                                        && (prefixPolicy != PrefixPolicy.FIRST_MESSAGE || prefixSetter.get())) {
                                    prefixSetter.set(false);
                                    return Component.text()
                                            .append(prefix)
                                            .append(Component.space())
                                            .append(component)
                                            .build();
                                } else {
                                    return component;
                                }
                            })
                            .collect(Collectors.toList());
                    if (list.isEmpty()) {
                        return List.of(translation.getFallback());
                    }
                    return list;
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Tries to get all the {@link Component} from this message for given sender.
     * This method will join the components together and each component will be on new line.
     *
     * @param sender sender to resolve the components for
     * @return component
     */
    public @NotNull Component getForJoined(@Nullable CommandSender sender) {
        return Component.join(Component.newLine(), getFor(sender));
    }

    /**
     * Tries to get all the {@link Component} from this message for anyone.
     * NOTE: this will resolve the message with the {@link LangService#getFallbackContainer()}
     *
     * @return list of components
     */
    public @NotNull List<@NotNull Component> getForAnyone() {
        return getFor(null);
    }

    /**
     * Tries to get all the {@link Component} from this message for anyone.
     * This method will join the components together and each component will be on new line.
     * NOTE: this will resolve the message with the {@link LangService#getFallbackContainer()}
     *
     * @return component
     */
    public @NotNull Component getForAnyoneJoined() {
        return Component.join(Component.newLine(), getForAnyone());
    }

    public <W extends CommandSender> @NotNull Message title(@NotNull W sender) {
        if (sender instanceof PlayerAudience) {
            ((PlayerAudience) sender).showTitle(asTitle(sender));
        }
        return this;
    }

    public <W extends CommandSender> @NotNull Message title(@NotNull W @NotNull... senders) {
        for (var sender : senders) {
            title(sender);
        }
        return this;
    }

    public <W extends CommandSender> @NotNull Message title(@NotNull Collection<@NotNull W> senders) {
        senders.forEach(this::title);
        return this;
    }

    /**
     * Sends this {@link Message} to defined receiver.
     *
     * @param receiver chosen one to receive the message.
     * @param <W>      type for {@link CommandSender}.
     * @return this message.
     */
    public <W extends CommandSender> @NotNull Message send(@NotNull W receiver) {
        getFor(receiver).forEach(receiver::sendMessage);
        return this;
    }

    /**
     * Sends this {@link Message} to all given receivers.
     *
     * @param receivers array of receivers
     * @param <W>       type for {@link CommandSender}.
     * @return this message.
     */
    public <W extends CommandSender> @NotNull Message send(@NotNull W @NotNull... receivers) {
        for (var sender : receivers) {
            send(sender);
        }
        return this;
    }

    /**
     * Sends this {@link Message} to all given receivers.
     *
     * @param receivers collection of receivers
     * @param <W>       type for {@link CommandSender}.
     * @return this message.
     */
    public <W extends CommandSender> @NotNull Message send(@NotNull Collection<@NotNull W> receivers) {
        receivers.forEach(this::send);
        return this;
    }

    /**
     * Transforms this message into a component.
     * This will process the message for given sender.
     *
     * @param audience sender to process this message for
     * @return {@link Component}.
     */
    @Override
    public @NotNull Component asComponent(@Nullable Audience audience) {
        return getForJoined(audience instanceof CommandSender ? (CommandSender) audience : null);
    }

    @Override
    public @NotNull List<Component> asComponentList(@Nullable Audience audience) {
        return getFor(audience instanceof CommandSender ? (CommandSender) audience : null);
    }

    @Override
    public @NotNull Component asComponent() {
        return getForAnyoneJoined();
    }

    @Override
    public @NotNull Title asTitle(@Nullable Audience sender, @Nullable TimesProvider times) {
        var messages = getFor(sender instanceof CommandSender ? (CommandSender) sender : null);
        return Title.title(messages.size() >= 1 ? messages.get(0) : Component.empty(), messages.size() >= 2 ? messages.get(1) : Component.empty(), times);
    }

    @Override
    public @NotNull Title asTitle(@Nullable Audience sender) {
        return asTitle(sender, times);
    }

    @Override
    public @NotNull Title asTitle(@Nullable TimesProvider times) {
        return asTitle(null, times);
    }

    @Override
    public @NotNull Title asTitle() {
        return asTitle(null, times);
    }

    public @NotNull TextEntry asTextEntry(@Nullable CommandSender wrapper) {
        return TextEntry.of(asComponent(wrapper));
    }

    public @NotNull TextEntry asTextEntry(@NotNull String identifier, @Nullable CommandSender wrapper) {
        return TextEntry.of(identifier, asComponent(wrapper));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull Message clone() {
        var msg = new Message(translations, langService, prefix);
        msg.times = times;
        msg.earlyPlaceholders.putAll(earlyPlaceholders);
        msg.placeholders.addAll(placeholders);
        msg.rawPlaceholders = rawPlaceholders;
        msg.prefixPolicy = prefixPolicy;
        msg.prefixResolving = prefixResolving;
        return msg;
    }

    /**
     * Policy for prefix resolving.
     */
    public enum PrefixPolicy {
        /**
         * All messages will have a prefix.
         */
        ALL_MESSAGES,
        /**
         * Only first message will have a prefix.
         */
        FIRST_MESSAGE
    }

    public enum PrefixResolving {
        DEFAULT,
        PER_PLAYER
    }

    public enum LinkPolicy {
        NO,
        ONLY_NON_TRANSLATIONS,
        ALL
    }
}
