package org.screamingsandals.lib.core.lang.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.util.*;

public class Message {
    private final Logger log;
    private final Map<String, String> placeholders = new HashMap<>();
    private SenderWrapper<?> sender;

    private String key;
    private boolean prefix;

    public Message(Logger log, String key,
                   boolean prefix, SenderWrapper<?> sender) {
        this.log = log;
        this.key = key;
        this.prefix = prefix;
        this.sender = sender;
    }

    public Message replace(String placeholder, String replacement) {
        placeholders.putIfAbsent(placeholder, replacement);
        return this;
    }

    public Message withPlaceholders(Map<String, String> replacements) {
        placeholders.putAll(replacements);
        return this;
    }

    public Message key(String key) {
        this.key = key;
        return this;
    }

    public Message clearReplaces() {
        this.placeholders.clear();
        return this;
    }

    public Message reset(String key) {
        return reset(key, false);
    }

    public Message reset(String key, boolean prefix) {
        this.key = key;
        this.prefix = prefix;

        return clearReplaces();
    }

    public Message prefix() {
        return prefix(true);
    }

    public Message prefix(boolean prefix) {
        this.prefix = prefix;
        return this;
    }

    public Message unprefix() {
        return prefix(false);
    }

    public Message sender(SenderWrapper<?> sender) {
        this.sender = sender;
        return this;
    }

    /**
     * This method returns only single component from
     * possible X components available.
     *
     * @return only first component found
     */
    public Component getFirst() {
        return getAll().stream().findFirst().orElse(Component.empty());
    }

    /**
     * This method returns only single component from
     * possible X components available.
     *
     * @param uuid UUID of the sender
     * @return only first component found
     */
    public Component getFirst(UUID uuid) {
        return getAll(uuid).stream().findFirst().orElse(Component.empty());
    }

    /**
     * This method returns all available components.
     * If no UUID is specified, we are using the default container.
     * Don't forget that even console has default language!
     *
     * @return all available components
     */
    public List<Component> getAll() {
        UUID uuid;

        if (sender == null || !sender.isPlayer()) {
            uuid = UUID.fromString(System.getProperty("slang.consoleUUID"));
        } else {
            final var testUUID = sender.getPlayer().getUuid();
            log.trace(testUUID.toString());
            uuid = sender.getPlayer().getUuid();
        }

        return getAll(uuid);
    }

    /**
     * This method returns all available components.
     * If no UUID is specified, we are using the default container.
     * Don't forget that even console has default language!
     *
     * @param uuid UUID of the sender
     * @return all available components
     */
    public List<Component> getAll(UUID uuid) {
        LanguageContainer container;

        if (uuid == null) {
            container = LanguageBase.getDefaultContainer();
        } else {
            container = LanguageBase.getPlayerRegistry().getFor(uuid)
                    .orElse(LanguageBase.getDefaultContainer());
        }

        return container.getMessages(key, prefix, placeholders, uuid);
    }

    public String toString() {
        final var builder = new StringBuilder();
        final var components = getAll();

        components.forEach(component -> {
            builder.append(LegacyComponentSerializer.legacyAmpersand().serialize(component));
            builder.append(System.lineSeparator());
        });

        return builder.toString();
    }

    public void send() {
        if (sender == null) {
            log.debug("trying to send the messages to {}", "sender not found");
            getAll().forEach(component -> LanguageBase.getPluginWrapper().getConsoleWrapper().sendMessage(component));
            return;
        }

        log.debug("trying to send the messages to {}", sender.toString());

        internalSend(sender, getAll());
    }

    public void send(SenderWrapper<?> sender) {
        this.sender = sender;
        send();
    }

    public void send(Collection<SenderWrapper<?>> senders) {
        senders.forEach(this::send);
    }

    protected void internalSend(SenderWrapper<?> sender, List<Component> message) {
        log.debug("Sending to [{}] with components [{}]", sender.toString(), message.toArray());
        message.forEach(sender::sendMessage);
    }
}
