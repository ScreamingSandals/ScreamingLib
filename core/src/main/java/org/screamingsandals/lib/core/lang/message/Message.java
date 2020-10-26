package org.screamingsandals.lib.core.lang.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.*;

public class Message {
    protected final Map<String, String> placeholders = new HashMap<>();

    protected String key;
    protected boolean prefix;
    protected SenderWrapper<?> sender;

    public Message(String key, boolean prefix, SenderWrapper<?> sender) {
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
            final var maybeContainer = LanguageBase.getPlayerRegistry().getFor(uuid);
            if (maybeContainer.isEmpty()) {
                return List.of();
            }

            container = maybeContainer.get();
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

    public Message send() {
        if (sender == null) {
            getAll().forEach(component -> LanguageBase.getPluginWrapper().getConsoleWrapper().sendMessage(component));
            return this;
        }

        internalSend(sender, getAll());
        return this;
    }

    public Message send(Object sender) {
        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                send(recipient);
            }
            return this;
        }

        if (!sender.getClass().isAssignableFrom(SenderWrapper.class)) {
            throw new UnsupportedOperationException("Sender needs to be SenderWrapper!");
        }

        final var castedSender = (SenderWrapper<?>) sender;

        internalSend(castedSender, getAll(castedSender.getPlayer().getUuid()));
        return this;
    }

    protected void internalSend(SenderWrapper<?> sender, List<Component> message) {
        message.forEach(sender::sendMessage);
    }
}
