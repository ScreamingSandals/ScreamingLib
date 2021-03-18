package org.screamingsandals.lib.lang;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Message {
    private static MiniMessage MINIMESSAGE = MiniMessage.builder()
            .markdown()
            .markdownFlavor(DiscordFlavor.get())
            .build();

    private final Translation translation;
    private final Map<String, Function<CommandSenderWrapper, Component>> placeholders = new HashMap<>();
    private final LangService langService;
    private Component prefix;

    public static Message of(String... key) {
        return new Message(Translation.of(key), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(Translation translation) {
        return new Message(translation, Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, String... key) {
        return new Message(Translation.of(key), langService, Component.empty());
    }

    public static Message of(LangService langService, Translation translation) {
        return new Message(translation, langService, Component.empty());
    }

    public static Message of(Component prefix, String... key) {
        return new Message(Translation.of(key), Lang.getDefaultService(), prefix);
    }

    public static Message of(Component prefix, Translation translation) {
        return new Message(translation, Lang.getDefaultService(), prefix);
    }

    public static Message of(LangService langService, Component prefix, String... key) {
        return new Message(Translation.of(key), langService, prefix);
    }

    public static Message of(LangService langService, Component prefix, Translation translation) {
        return new Message(translation, langService, prefix);
    }

    public static Message of(Collection<String> key) {
        return new Message(Translation.of(key), Lang.getDefaultService(), Component.empty());
    }

    public static Message of(LangService langService, Collection<String> key) {
        return new Message(Translation.of(key), langService, Component.empty());
    }

    public static Message of(Component prefix, Collection<String> key) {
        return new Message(Translation.of(key), Lang.getDefaultService(), prefix);
    }

    public static Message of(LangService langService, Component prefix, Collection<String> key) {
        return new Message(Translation.of(key), langService, prefix);
    }

    public Message placeholder(String placeholder, byte value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, short value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, int value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, long value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, char value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, boolean value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, double value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, float value) {
        return placeholder(placeholder, String.valueOf(value));
    }

    public Message placeholder(String placeholder, double value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, String.valueOf(Math.round(value * pow) / pow));
    }

    public Message placeholder(String placeholder, float value, int round) {
        double pow = Math.pow(10, round);
        return placeholder(placeholder, String.valueOf(Math.round(value * pow) / pow));
    }

    public Message placeholder(String placeholder, String value) {
        return placeholder(placeholder, MINIMESSAGE.parse(value));
    }

    public Message placeholder(String placeholder, Component component) {
        placeholders.put(placeholder, sender -> component);
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

    public Message noPrefix() {
        this.prefix = Component.empty();
        return this;
    }

    public Message defaultPrefix() {
        this.prefix = Lang.getDefaultPrefix();
        return this;
    }

    public List<Component> getFor(CommandSenderWrapper sender) {
        var list = langService
                .getFor(sender)
                .translate(translation.getKeys())
                .stream()
                .map(s -> MINIMESSAGE.parse(s, placeholders
                        .entrySet()
                        .stream()
                        .map(entry -> Template.of(entry.getKey(), entry.getValue().apply(sender)))
                        .collect(Collectors.toList())
                ))
                .map(component -> {
                    if (!Component.empty().equals(prefix)) {
                        return Component.text().append(prefix).append(Component.space()).append(component).build();
                    } else {
                        return component;
                    }
                })
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return List.of(translation.getFallback());
        }
        return list;
    }

    public List<Component> getForAnyone() {
        return getFor(null);
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
}
