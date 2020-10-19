package org.screamingsandals.lib.core.lang.message;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;
import org.screamingsandals.lib.core.reflect.SReflect;

import java.util.*;

public class Message {
    protected final Map<String, Object> placeholders = new HashMap<>();

    protected String key;
    protected boolean prefix;
    protected Object player;

    public Message(String key, boolean prefix, Object player) {
        this.key = key;
        this.prefix = prefix;
        this.player = player;
    }

    public Message replace(String placeholder, Object replacement) {
        placeholders.putIfAbsent(placeholder, replacement);
        return this;
    }

    public Message withPlaceholders(Map<String, Object> replacements) {
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

    public List<TextComponent> get() {
        UUID uuid;

        if (player == null) {
            uuid = UUID.fromString(System.getProperty("slang.consoleUUID"));
        } else {
            uuid = internalGetUuid(player);
        }

        return get(uuid);
    }

    public List<TextComponent> get(UUID uuid) {
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
        final var components = get();

        components.forEach(component -> {
            builder.append(component.toLegacyText());
            builder.append(System.lineSeparator());
        });

        return builder.toString();
    }

    public Message send() {
        if (player == null) {
            get().forEach(component -> LanguageBase.getPluginCore().sendMessage(component));
            return this;
        }

        internalSend(player, get());
        return this;
    }

    public Message send(Object sender) {
        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                send(recipient);
            }
            return this;
        }

        internalSend(sender, get(internalGetUuid(sender)));
        return this;
    }

    public Message send(Object sender, String permissions) {
        if (sender instanceof Collection) {
            for (Object recipient : (Collection<?>) sender) {
                send(recipient, permissions);
            }
            return this;
        }

        try {
            boolean hasPermissions = (boolean) SReflect.getMethod(sender, "hasPermission", String.class).invoke(permissions);
            if (hasPermissions) {
                internalSend(sender, get(internalGetUuid(sender)));
            }
        } catch (Throwable ignored) {
        }
        return this;
    }

    public Message sendActionBar(Object sender) {
        if (sender instanceof Collection) {
            for (var rec : (Collection<?>) sender) {
                sendActionBar(rec);
            }
            return this;
        }

        get(internalGetUuid(sender)).forEach(component ->
                SReflect.getMethod(sender, "sendMessage", ChatMessageType.class, TextComponent.class).invoke(ChatMessageType.ACTION_BAR, component));
        return this;
    }

    protected void internalSend(Object sender, List<TextComponent> message) {
        message.forEach(component ->
                SReflect.getMethod(sender, "sendMessage", TextComponent.class).invoke(message));
    }

    protected UUID internalGetUuid(Object player) {
        Preconditions.checkNotNull(player);
        return (UUID) SReflect.fastInvoke(player, "getUniqueId");
    }
}
