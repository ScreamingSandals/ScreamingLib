package org.screamingsandals.lib.lang;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.TitleableSenderMessage;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public final class Message implements TitleableSenderMessage, Cloneable {
    private final List<Translation> translations = new LinkedList<>();
    private final Map<String, Function<CommandSenderWrapper, Component>> placeholders = new HashMap<>();
    private final LangService langService;
    @NotNull
    private Component prefix;
    @Nullable
    private Title.Times times;
    private PrefixPolicy prefixPolicy = PrefixPolicy.ALL_MESSAGES;

    public Message(Collection<Translation> translations, LangService langService, @NotNull Component prefix) {
        this.translations.addAll(translations);
        this.langService = langService;
        this.prefix = prefix;
    }

    public static Message of(String... key) {
        return new Message(Collections.singletonList(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(Translation translation) {
        return new Message(Collections.singletonList(translation), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(List<Translation> translations) {
        return new Message(translations, Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, String... key) {
        return new Message(Collections.singletonList(Translation.of(key)), langService, Component.empty());
    }

    public static Message of(LangService langService, Translation translation) {
        return new Message(Collections.singletonList(translation), langService, Component.empty());
    }

    public static Message of(LangService langService, List<Translation> translations) {
        return new Message(translations, langService, Component.empty());
    }

    public static Message of(Component prefix, String... key) {
        return new Message(Collections.singletonList(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    public static Message of(Component prefix, Translation translation) {
        return new Message(Collections.singletonList(translation), Lang.getDefaultService(), prefix);
    }

    public static Message of(Component prefix, List<Translation> translations) {
        return new Message(translations, Lang.getDefaultService(), prefix);
    }

    public static Message of(LangService langService, Component prefix, String... key) {
        return new Message(Collections.singletonList(Translation.of(key)), langService, prefix);
    }

    public static Message of(LangService langService, Component prefix, Translation translation) {
        return new Message(Collections.singletonList(translation), langService, prefix);
    }

    public static Message of(LangService langService, Component prefix, List<Translation> translations) {
        return new Message(translations, langService, prefix);
    }

    public static Message of(Collection<String> key) {
        return new Message(Collections.singletonList(Translation.of(key)), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, Collection<String> key) {
        return new Message(Collections.singletonList(Translation.of(key)), langService, Component.empty());
    }

    public static Message of(Component prefix, Collection<String> key) {
        return new Message(Collections.singletonList(Translation.of(key)), Lang.getDefaultService(), prefix);
    }

    public static Message of(LangService langService, Component prefix, Collection<String> key) {
        return new Message(Collections.singletonList(Translation.of(key)), langService, prefix);
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

    public Message prefix(Component prefix) {
        if (prefix == null) {
            return noPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    public Message prefixOrDefault(Component prefix) {
        if (prefix == null) {
            return defaultPrefix();
        }
        this.prefix = prefix;
        return this;
    }

    public Message noPrefix() {
        this.prefix = Component.empty();
        return this;
    }

    public Message defaultPrefix() {
        this.prefix = Lang.getDefaultPrefix();
        return this;
    }

    public Message prefixPolicy(PrefixPolicy prefixPolicy) {
        this.prefixPolicy = prefixPolicy;
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

    public Message join(Translation translation) {
        this.translations.add(translation);
        return this;
    }

    public Message join(List<Translation> translations) {
        this.translations.addAll(translations);
        return this;
    }

    public Message times(Title.Times times) {
        this.times = times;
        return this;
    }

    public List<Component> getFor(CommandSenderWrapper sender) {
        final var atomic = new AtomicBoolean(true);
        final var container = langService.getFor(sender);

        return translations
                .stream()
                .map(translation -> {
                    final var list = container
                            .translate(translation.getKeys())
                            .stream()
                            .map(s -> Lang.MINIMESSAGE.parse(s, placeholders
                                    .entrySet()
                                    .stream()
                                    .map(entry -> Template.of(entry.getKey(), entry.getValue().apply(sender)))
                                    .collect(Collectors.toList())
                            ))
                            .map(component -> {
                                if (!Component.empty().equals(prefix) && (prefixPolicy != PrefixPolicy.FIRST_MESSAGE || atomic.get())) {
                                    atomic.set(false);
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
    public @NotNull TextEntry asTextEntry(@Nullable CommandSenderWrapper wrapper) {
        return TextEntry.of(asComponent(wrapper));
    }

    @Override
    public @NotNull TextEntry asTextEntry(@NotNull String identifier, CommandSenderWrapper wrapper) {
        return TextEntry.of(identifier, asComponent(wrapper));
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
    protected Message clone() {
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
}
