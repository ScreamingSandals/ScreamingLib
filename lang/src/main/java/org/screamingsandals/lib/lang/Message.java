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

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.placeholders.PlaceholderManager;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.sender.TitleableSenderMessage;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Translated message.
 */
@Data
public class Message implements TitleableSenderMessage, Cloneable {
    private static final Pattern LEGACY_PLACEHOLDERS = Pattern.compile("[%]([^%]+)[%]");
    private static final Pattern EARLY_MINI_MESSAGE_PLACEHOLDERS = Pattern.compile("[<]([^>]+)[>]");

    private final List<Messageable> translations = new LinkedList<>();
    private final Map<String, Function<CommandSenderWrapper, Component>> placeholders = new HashMap<>();
    private final Map<String, String> earlyPlaceholders = new HashMap<>();
    private final LangService langService;
    @NotNull
    private Component prefix;
    @Nullable
    private Title.Times times;
    private PrefixPolicy prefixPolicy = PrefixPolicy.ALL_MESSAGES;
    private PrefixResolving prefixResolving = PrefixResolving.DEFAULT;

    public <M extends Messageable> Message(Collection<M> translations, LangService langService, @NotNull Component prefix) {
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
        return new Message(List.of(), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new empty Message.
     *
     * @param prefix custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull Component prefix) {
        return new Message(List.of(), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new empty Message.
     *
     * @param prefix custom prefix to use
     * @return empty message.
     */
    public static @NotNull Message empty(@NotNull ComponentLike prefix) {
        return new Message(List.of(), Lang.getDefaultService(), prefix.asComponent());
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
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull String message) {
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static Message ofPlainText(ComponentLike prefix, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix   custom prefix to use
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix.asComponent());
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service  custom {@link LangService} to use.
     * @param messages input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull List<String> messages) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull String... messages) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String... messages) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull List<String> messages) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull Component prefix, @NotNull Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param prefix  custom prefix to use
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull ComponentLike prefix, @NotNull Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from plain text.
     *
     * @param service custom {@link LangService} to use.
     * @param message supplier of input text
     * @return new message.
     */
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Supplier<List<String>> message) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull Component prefix, @NotNull Supplier<List<String>> message) {
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
    public static @NotNull Message ofPlainText(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param key key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Translation}
     *
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Translation translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service custom {@link LangService} to use.
     * @param key     key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull String... key) {
        return new Message(List.of(Translation.of(key)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service     custom {@link LangService} to use.
     * @param translation key of the translation
     * @return new message.
     */
    public static Message of(LangService service, Translation translation) {
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
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull List<M> translations) {
        return new Message(translations, service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix custom prefix to use
     * @param key    key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix custom prefix to use
     * @param key    key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix      custom prefix to use
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param prefix      custom prefix to use
     * @param translation key of the translation
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull Messageable translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull Component prefix, @NotNull List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix       custom prefix to use
     * @param translations translation keys
     * @param <M>          type that extends {@link Messageable}
     * @return new message.
     */
    public static <M extends Messageable> @NotNull Message of(@NotNull ComponentLike prefix, @NotNull List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), prefix.asComponent());
    }


    /**
     * Creates new {@link Message} from given Translation key.
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull String... key) {
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
    public static @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull String... key) {
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
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull List<M> translations) {
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
    public static <M extends Messageable> @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull List<M> translations) {
        return new Message(translations, service, prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param key translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service custom {@link LangService} to use.
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Collection<String> key) {
        return new Message(List.of(Translation.of(key)), service, Component.empty());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix custom prefix to use
     * @param key    translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull Component prefix, @NotNull Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param prefix custom prefix to use
     * @param key    translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull ComponentLike prefix, @NotNull Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix.asComponent());
    }

    /**
     * Creates new {@link Message} from given {@link Messageable}
     *
     * @param service custom {@link LangService} to use.
     * @param prefix  custom prefix to use
     * @param key     translation keys
     * @return new message.
     */
    public static @NotNull Message of(@NotNull LangService service, @NotNull Component prefix, @NotNull Collection<String> key) {
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
    public static @NotNull Message of(@NotNull LangService service, @NotNull ComponentLike prefix, @NotNull Collection<String> key) {
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
    public @NotNull Message placeholder(@NotNull String placeholder, byte value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, short value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, int value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, long value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, char value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, boolean value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, double value) {
        return placeholder(placeholder, Component.text(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, float value) {
        return placeholder(placeholder, Component.text(value));
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
    public @NotNull Message placeholder(@NotNull String placeholder, double value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, Component.text(Math.round(value * pow) / pow));
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
    public @NotNull Message placeholder(@NotNull String placeholder, float value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, Component.text(Math.round(value * pow) / pow));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param value       placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull String value) {
        return placeholder(placeholder, Lang.MINIMESSAGE.parse(value));
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param component   placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull Component component) {
        placeholders.put(placeholder, sender -> component);
        return this;
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param component   placeholder value
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull ComponentLike component) {
        return placeholder(placeholder, component.asComponent());
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param translation placeholder value, resolved from {@link Translation}
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull Translation translation) {
        var msg = of(translation);
        placeholders.put(placeholder, msg::getForJoined);
        return this;
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder placeholder key
     * @param message     placeholder value, resolved from {@link Message}
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull Message message) {
        placeholders.put(placeholder, message::getForJoined);
        return this;
    }

    /**
     * Registers new placeholder.
     * Used for replacing placeholders before constructing the message.
     *
     * @param placeholder       placeholder key
     * @param componentFunction function that returns a {@link Component} as the placeholder value.
     * @return this message
     */
    public @NotNull Message placeholder(@NotNull String placeholder, @NotNull Function<CommandSenderWrapper, Component> componentFunction) {
        placeholders.put(placeholder, componentFunction);
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
    public @NotNull Message prefix(ComponentLike prefix) {
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
    public @NotNull Message prefixOrDefault(Component prefix) {
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
    public @NotNull Message prefixOrDefault(ComponentLike prefix) {
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
        this.prefix = Lang.getDefaultService().getDefaultPrefix();
        return this;
    }

    /**
     * Uses the {@link PrefixResolving#PER_PLAYER} prefix resolving policy.
     * This means that the prefix is resolved per-player from {@link LangService#resolvePrefix(CommandSenderWrapper)}.
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
    public @NotNull Message prefixPolicy(PrefixPolicy prefixPolicy) {
        this.prefixPolicy = prefixPolicy;
        return this;
    }

    /**
     * Sets new prefix resolving policy.
     * If the policy is {@link PrefixResolving#PER_PLAYER}, prefix is resolved per-player from {@link LangService#resolvePrefix(CommandSenderWrapper)}.
     *
     * @return self
     */
    public @NotNull Message prefixResolving(PrefixResolving resolving) {
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
    public @NotNull Message join(String... keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    /**
     * Joins given keys into this message.
     *
     * @param keys keys to join
     * @return self
     */
    public @NotNull Message join(Collection<String> keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param message message to join
     * @return self
     */
    public @NotNull Message joinPlainText(String message) {
        this.translations.add(StringMessageable.of(message));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinPlainText(List<String> messages) {
        this.translations.add(StringMessageable.of(messages));
        return this;
    }

    /**
     * Joins given plain text into this message.
     *
     * @param messages messages to join
     * @return self
     */
    public @NotNull Message joinPlainText(Supplier<List<String>> messages) {
        this.translations.add(SupplierStringMessageable.of(messages));
        return this;
    }

    /**
     * Joins given translation into this message.
     *
     * @param translation translation to join
     * @return self
     */
    public @NotNull Message join(Messageable translation) {
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
    public @NotNull <M extends Messageable> Message join(List<M> translations) {
        this.translations.addAll(translations);
        return this;
    }

    /**
     * Sets the {@link Title.Times} for this message.
     *
     * @param times times to set
     * @return self
     */
    public @NotNull Message times(Title.Times times) {
        this.times = times;
        return this;
    }

    /**
     * Tries to get all the {@link Component} from this message for given sender.
     *
     * @param sender sender to resolve the components for
     * @return list of components.
     */
    public @NotNull List<Component> getFor(CommandSenderWrapper sender) {
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
                                            output.append(Lang.MINIMESSAGE.serialize(AdventureHelper.toComponent(result)));
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

                                    final var resolvedTemplates = TemplateResolver
                                            .templates(placeholders
                                                    .entrySet()
                                                    .stream()
                                                    .map(entry -> Template.template(entry.getKey(), entry.getValue().apply(sender)))
                                                    .collect(Collectors.toList()));

                                    return Lang.MINIMESSAGE.deserialize(s, resolvedTemplates);
                                } else {
                                    // Black magic again
                                    // SKIP THIS.
                                    var matcher = LEGACY_PLACEHOLDERS.matcher(s);

                                    var lastIndex = 0;
                                    var output = new StringBuilder();
                                    while (matcher.find()) {
                                        output.append(s, lastIndex, matcher.start());
                                        if (placeholders.containsKey(matcher.group(1))) {
                                            output.append(AdventureHelper.toLegacy(placeholders.get(matcher.group(1)).apply(sender)));
                                        } else {
                                            output.append("%").append(matcher.group(1)).append("%");
                                        }

                                        lastIndex = matcher.end();
                                    }
                                    if (lastIndex < s.length()) {
                                        output.append(s, lastIndex, s.length());
                                    }
                                    return AdventureHelper.toComponent(output.toString());
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
    public @NotNull Component getForJoined(CommandSenderWrapper sender) {
        return Component.join(JoinConfiguration.separator(Component.newline()), getFor(sender));
    }

    /**
     * Tries to get all the {@link Component} from this message for anyone.
     * NOTE: this will resolve the message with the {@link LangService#getFallbackContainer()}
     *
     * @return list of components
     */
    public @NotNull List<Component> getForAnyone() {
        return getFor(null);
    }

    /**
     * Tries to get all the {@link Component} from this message for anyone.
     * This method will join the components together and each component will be on new line.
     * NOTE: this will resolve the message with the {@link LangService#getFallbackContainer()}
     *
     * @return component
     */
    public Component getForAnyoneJoined() {
        return Component.join(JoinConfiguration.separator(Component.newline()), getForAnyone());
    }

    public <W extends CommandSenderWrapper> Message title(W sender) {
        sender.showTitle(asTitle(sender));
        return this;
    }

    public <W extends CommandSenderWrapper> Message title(W... senders) {
        for (var sender : senders) {
            title(sender);
        }
        return this;
    }

    public <W extends CommandSenderWrapper> Message title(Collection<W> senders) {
        senders.forEach(this::title);
        return this;
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder titleTask(W sender) {
        return Tasker
                .build(() -> sender.showTitle(asTitle(sender)));
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder titleTask(W... senders) {
        return Tasker
                .build(() -> {
                    for (var sender : senders) {
                        title(sender);
                    }
                });
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder titleTask(Collection<W> senders) {
        return Tasker
                .build(() -> senders.forEach(this::title));
    }

    public <W extends CommandSenderWrapper> Message titleAsync(W sender) {
        titleTask(sender)
                .async()
                .start();
        return this;
    }

    public <W extends CommandSenderWrapper> Message titleAsync(W... senders) {
        titleTask(senders)
                .async()
                .start();
        return this;
    }

    public <W extends CommandSenderWrapper> Message titleAsync(Collection<W> senders) {
        titleTask(senders)
                .async()
                .start();
        return this;
    }

    public <W extends CommandSenderWrapper> Message send(W sender) {
        getFor(sender).forEach(sender::sendMessage);
        return this;
    }

    public <W extends CommandSenderWrapper> Message send(W... senders) {
        for (var sender : senders) {
            send(sender);
        }
        return this;
    }

    public <W extends CommandSenderWrapper> Message send(Collection<W> senders) {
        senders.forEach(this::send);
        return this;
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder sendTask(W sender) {
        return Tasker
                .build(() -> getFor(sender).forEach(sender::sendMessage));
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder sendTask(W... senders) {
        return Tasker
                .build(() -> {
                    for (var sender : senders) {
                        send(sender);
                    }
                });
    }

    public <W extends CommandSenderWrapper> Tasker.TaskBuilder sendTask(Collection<W> senders) {
        return Tasker
                .build(() -> senders.forEach(this::send));
    }

    public <W extends CommandSenderWrapper> Message sendAsync(W sender) {
        sendTask(sender)
                .async()
                .start();
        return this;
    }

    public <W extends CommandSenderWrapper> Message sendAsync(W... senders) {
        sendTask(senders)
                .async()
                .start();
        return this;
    }

    public <W extends CommandSenderWrapper> Message sendAsync(Collection<W> senders) {
        sendTask(senders)
                .async()
                .start();
        return this;
    }

    @Override
    @NotNull
    public Component asComponent(@Nullable CommandSenderWrapper sender) {
        return getForJoined(sender);
    }

    @Override
    @NotNull
    public List<Component> asComponentList(@Nullable CommandSenderWrapper wrapper) {
        return getFor(wrapper);
    }

    @Override
    @NotNull
    public Component asComponent() {
        return getForAnyoneJoined();
    }

    @Override
    @NotNull
    public Title asTitle(@Nullable CommandSenderWrapper sender, @Nullable Title.Times times) {
        var messages = getFor(sender);
        return Title.title(messages.size() >= 1 ? messages.get(0) : Component.empty(), messages.size() >= 2 ? messages.get(1) : Component.empty(), times);
    }

    @Override
    @NotNull
    public Title asTitle(@Nullable CommandSenderWrapper sender) {
        return asTitle(sender, times);
    }

    @Override
    @NotNull
    public Title asTitle(@Nullable Title.Times times) {
        return asTitle(null, times);
    }

    @Override
    @NotNull
    public Title asTitle() {
        return asTitle(null, times);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Message clone() {
        var msg = new Message(translations, langService, prefix);
        msg.times = times;
        msg.placeholders.putAll(placeholders);
        msg.prefixPolicy = prefixPolicy;
        return msg;
    }

    public enum PrefixPolicy {
        ALL_MESSAGES,
        FIRST_MESSAGE
    }

    public enum PrefixResolving {
        DEFAULT,
        PER_PLAYER
    }
}
