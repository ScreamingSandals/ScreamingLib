package org.screamingsandals.lib.lang;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Template;
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

    public static Message empty() {
        return new Message(List.of(), Lang.getDefaultService(), Component.empty());
    }

    public static Message empty(Component prefix) {
        return new Message(List.of(), Lang.getDefaultService(), prefix);
    }

    public static Message empty(ComponentLike prefix) {
        return new Message(List.of(), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message empty(LangService service) {
        return new Message(List.of(), service, Component.empty());
    }

    public static Message empty(LangService service, Component prefix) {
        return new Message(List.of(), service, prefix);
    }

    public static Message empty(LangService service, ComponentLike prefix) {
        return new Message(List.of(), service, prefix.asComponent());
    }

    public static Message ofPlainText(String message) {
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), Component.empty());
    }

    public static Message ofPlainText(String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), Component.empty());
    }

    public static Message ofPlainText(List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), Component.empty());
    }

    public static Message ofPlainText(Component prefix, String message) {
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), prefix);
    }

    public static Message ofPlainText(ComponentLike prefix, String message) {
        return new Message(List.of(StringMessageable.of(message)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message ofPlainText(Component prefix, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix);
    }

    public static Message ofPlainText(ComponentLike prefix, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message ofPlainText(Component prefix, List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix);
    }

    public static Message ofPlainText(ComponentLike prefix, List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message ofPlainText(LangService service, String message) {
        return new Message(List.of(StringMessageable.of(message)), service, Component.empty());
    }

    public static Message ofPlainText(LangService service, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, Component.empty());
    }

    public static Message ofPlainText(LangService service, List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, Component.empty());
    }

    public static Message ofPlainText(LangService service, Component prefix, String message) {
        return new Message(List.of(StringMessageable.of(message)), service, prefix);
    }

    public static Message ofPlainText(LangService service, ComponentLike prefix, String message) {
        return new Message(List.of(StringMessageable.of(message)), service, prefix.asComponent());
    }

    public static Message ofPlainText(LangService service, Component prefix, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix);
    }

    public static Message ofPlainText(LangService service, ComponentLike prefix, String... messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix.asComponent());
    }

    public static Message ofPlainText(LangService service, Component prefix, List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix);
    }

    public static Message ofPlainText(LangService service, ComponentLike prefix, List<String> messages) {
        return new Message(List.of(StringMessageable.of(messages)), service, prefix.asComponent());
    }

    public static Message ofPlainText(Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), Component.empty());
    }

    public static Message ofPlainText(Component prefix, Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), prefix);
    }

    public static Message ofPlainText(ComponentLike prefix, Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message ofPlainText(LangService service, Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, Component.empty());
    }

    public static Message ofPlainText(LangService service, Component prefix, Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, prefix);
    }

    public static Message ofPlainText(LangService service, ComponentLike prefix, Supplier<List<String>> message) {
        return new Message(List.of(SupplierStringMessageable.of(message)), service, prefix.asComponent());
    }

    public static Message of(String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(Translation translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), Component.empty());
    }

    public static <M extends Messageable> Message of(List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, String... key) {
        return new Message(List.of(Translation.of(key)), langService, Component.empty());
    }

    public static Message of(LangService langService, Translation translation) {
        return new Message(List.of(translation), langService, Component.empty());
    }

    public static <M extends Messageable> Message of(LangService langService, List<M> translations) {
        return new Message(translations, langService, Component.empty());
    }

    public static Message of(Component prefix, String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    public static Message of(ComponentLike prefix, String... key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message of(Component prefix, Messageable translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), prefix);
    }

    public static Message of(ComponentLike prefix, Messageable translation) {
        return new Message(List.of(translation), Lang.getDefaultService(), prefix.asComponent());
    }

    public static <M extends Messageable> Message of(Component prefix, List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), prefix);
    }

    public static <M extends Messageable> Message of(ComponentLike prefix, List<M> translations) {
        return new Message(translations, Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message of(LangService langService, Component prefix, String... key) {
        return new Message(List.of(Translation.of(key)), langService, prefix);
    }

    public static Message of(LangService langService, ComponentLike prefix, String... key) {
        return new Message(List.of(Translation.of(key)), langService, prefix.asComponent());
    }

    public static Message of(LangService langService, Component prefix, Messageable translation) {
        return new Message(List.of(translation), langService, prefix);
    }

    public static Message of(LangService langService, ComponentLike prefix, Messageable translation) {
        return new Message(List.of(translation), langService, prefix.asComponent());
    }

    public static <M extends Messageable> Message of(LangService langService, Component prefix, List<M> translations) {
        return new Message(translations, langService, prefix);
    }

    public static <M extends Messageable> Message of(LangService langService, ComponentLike prefix, List<M> translations) {
        return new Message(translations, langService, prefix.asComponent());
    }

    public static Message of(Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, Collection<String> key) {
        return new Message(List.of(Translation.of(key)), langService, Component.empty());
    }

    public static Message of(Component prefix, Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    public static Message of(ComponentLike prefix, Collection<String> key) {
        return new Message(List.of(Translation.of(key)), Lang.getDefaultService(), prefix.asComponent());
    }

    public static Message of(LangService langService, Component prefix, Collection<String> key) {
        return new Message(List.of(Translation.of(key)), langService, prefix);
    }

    public static Message of(LangService langService, ComponentLike prefix, Collection<String> key) {
        return new Message(List.of(Translation.of(key)), langService, prefix.asComponent());
    }

    public Message placeholder(String placeholder, byte value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, short value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, int value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, long value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, char value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, boolean value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, double value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, float value) {
        return placeholder(placeholder, Component.text(value));
    }

    public Message placeholder(String placeholder, double value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, Component.text(Math.round(value * pow) / pow));
    }

    public Message placeholder(String placeholder, float value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, Component.text(Math.round(value * pow) / pow));
    }

    public Message placeholder(String placeholder, String value) {
        return placeholder(placeholder, Lang.MINIMESSAGE.parse(value));
    }

    public Message placeholder(String placeholder, Component component) {
        placeholders.put(placeholder, sender -> component);
        return this;
    }

    public Message placeholder(String placeholder, ComponentLike component) {
        placeholders.put(placeholder, sender -> component.asComponent());
        return this;
    }

    public Message placeholder(String placeholder, Translation translation) {
        var msg = of(translation);
        placeholders.put(placeholder, msg::getForJoined);
        return this;
    }

    public Message placeholder(String placeholder, Message message) {
        placeholders.put(placeholder, message::getForJoined);
        return this;
    }

    public Message placeholder(String placeholder, Function<CommandSenderWrapper, Component> componentFunction) {
        placeholders.put(placeholder, componentFunction);
        return this;
    }

    /**
     * This method works only with Messages using MiniMessage format.
     * It replaces the placeholder BEFORE the message is processed by MiniMessage,
     * so you can change format for whole message, not just the inserted part.
     *
     * @param placeholder Placeholder
     * @param value Component which will replace the placeholder
     * @return self
     */
    public Message earlyPlaceholder(String placeholder, Component value) {
        earlyPlaceholders.put(placeholder, Lang.MINIMESSAGE.serialize(value));
        return this;
    }

    /**
     * This method works only with Messages using MiniMessage format.
     * It replaces the placeholder BEFORE the message is processed by MiniMessage,
     * so you can change format for whole message, not just the inserted part.
     *
     * @param placeholder Placeholder
     * @param value String which will replace the placeholder. It must be in MiniMessage format
     * @return self
     */
    public Message earlyPlaceholder(String placeholder, String value) {
        earlyPlaceholders.put(placeholder, value);
        return this;
    }

    public Message prefix(Component prefix) {
        if (prefix == null) {
            return noPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    public Message prefix(ComponentLike prefix) {
        if (prefix == null) {
            return noPrefix();
        }
        this.prefix = prefix.asComponent();
        return this;
    }

    public Message prefixOrDefault(Component prefix) {
        if (prefix == null || Component.empty().equals(prefix)) {
            return defaultPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    public Message prefixOrDefault(ComponentLike prefix) {
        if (prefix == null || Component.empty().equals(prefix.asComponent())) {
            return defaultPrefix();
        }
        this.prefix = prefix.asComponent();
        return this;
    }

    public Message noPrefix() {
        this.prefix = Component.empty();
        return this;
    }

    public Message defaultPrefix() {
        this.prefix = Lang.getDefaultService().getDefaultPrefix();
        return this;
    }

    public Message resolvePrefix() {
        this.prefixResolving = PrefixResolving.PER_PLAYER;
        return this;
    }

    public Message prefixPolicy(PrefixPolicy prefixPolicy) {
        this.prefixPolicy = prefixPolicy;
        return this;
    }

    public Message prefixResolving(PrefixResolving resolving) {
        this.prefixResolving = resolving;
        return this;
    }

    public Message join(String key) {
        this.translations.add(Translation.of(key));
        return this;
    }

    public Message join(String... keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    public Message join(Collection<String> keys) {
        this.translations.add(Translation.of(keys));
        return this;
    }

    public Message joinPlainText(String message) {
        this.translations.add(StringMessageable.of(message));
        return this;
    }

    public Message joinPlainText(List<String> messages) {
        this.translations.add(StringMessageable.of(messages));
        return this;
    }

    public Message joinPlainText(Supplier<List<String>> messages) {
        this.translations.add(SupplierStringMessageable.of(messages));
        return this;
    }

    public Message join(Messageable translation) {
        this.translations.add(translation);
        return this;
    }

    public <M extends Messageable> Message join(List<M> translations) {
        this.translations.addAll(translations);
        return this;
    }

    public Message times(Title.Times times) {
        this.times = times;
        return this;
    }

    public List<Component> getFor(CommandSenderWrapper sender) {
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

                                    return Lang.MINIMESSAGE.parse(s, placeholders
                                            .entrySet()
                                            .stream()
                                            .map(entry -> Template.of(entry.getKey(), entry.getValue().apply(sender)))
                                            .collect(Collectors.toList()));
                                } else {
                                    // Black magic again
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

    public Component getForJoined(CommandSenderWrapper sender) {
        return Component.join(Component.newline(), getFor(sender));
    }

    public List<Component> getForAnyone() {
        return getFor(null);
    }

    public Component getForAnyoneJoined() {
        return Component.join(Component.newline(), getForAnyone());
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
