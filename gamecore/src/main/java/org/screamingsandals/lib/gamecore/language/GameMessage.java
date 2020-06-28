package org.screamingsandals.lib.gamecore.language;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.lang.message.Message;
import org.screamingsandals.lib.lang.storage.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.lib.reflection.Reflection.*;

public class GameMessage extends Message {
    private GameFrame gameFrame;

    public GameMessage(String key, Storage storage) {
        super(key, storage, (String) null, false);
    }

    public GameMessage(String key, Storage storage, boolean prefix) {
        super(key, storage, (String) null, prefix);
    }

    public GameMessage(String key, Storage storage, String def) {
        super(key, storage, def, false);
    }

    public GameMessage(String key, Storage storage, String def, boolean prefix) {
        super(key, storage, def, prefix);
    }

    public GameMessage(String key, Storage storage, List<String> defList) {
        super(key, storage, defList, false);
    }

    public GameMessage(String key, Storage storage, List<String> defList, boolean prefix) {
        super(key, storage, defList, prefix);
    }

    public GameMessage game(GameFrame gameFrame) {
        if (gameFrame == null || gameFrame.getPlaceholderParser() == null) {
            return this;
        }

        this.gameFrame = gameFrame;
        final var parser = gameFrame.getPlaceholderParser();
        parser.update();

        replaces.putAll(gameFrame.getPlaceholderParser().getAvailable());
        return this;
    }

    @Override
    public GameMessage replace(String placeholder, Object replacement) {
        replaces.put(placeholder, replacement);
        return this;
    }

    @Override
    public GameMessage replaceAll(Map<String, Object> replacements) {
        replaces.putAll(replacements);
        return this;
    }

    @Override
    public GameMessage key(String key) {
        this.key = key;
        return this;
    }

    @Override
    public GameMessage def(String def) {
        this.def = def;
        return this;
    }

    @Override
    public GameMessage clearReplaces() {
        this.replaces.clear();
        return this;
    }

    @Override
    public GameMessage reset(String key) {
        return reset(key, null, false);
    }

    @Override
    public GameMessage reset(String key, String def) {
        return reset(key, def, false);
    }

    @Override
    public GameMessage reset(String key, String def, boolean prefix) {
        this.key = key;
        this.def = def;
        this.prefix = prefix;

        return clearReplaces();
    }

    @Override
    public GameMessage prefix() {
        return prefix(true);
    }

    @Override
    public GameMessage prefix(boolean prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public GameMessage unprefix() {
        return prefix(false);
    }

    @Override
    public GameMessage setStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    @Override
    public GameMessage setLanguage(Object player) {
        super.setLanguage(player);
        return this;
    }

    @Override
    public GameMessage setLanguage(String language) {
        super.setLanguage(language);
        return this;
    }

    @Override
    public String get() {
        try {
            String message = storage.translate(key, def, prefix);

            for (var replace : replaces.entrySet()) {
                message = message.replaceAll(replace.getKey(), replace.getValue().toString());
            }

            return message;
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    public List<String> getList() {
        try {
            final List<String> messages = storage.translateList(key, defList, prefix);
            final List<String> toReturn = new ArrayList<>();

            for (String toTranslate : messages) {
                for (var replace : replaces.entrySet()) {
                    toTranslate = toTranslate.replaceAll(replace.getKey(), replace.getValue().toString());
                }
                toReturn.add(toTranslate);
            }

            return toReturn;
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    public String toString() {
        return get();
    }

    @Override
    public GameMessage send(Object sender) {
        if (sender == null) {
            return this;
        }

        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                send(recipient);
            }
            return this;
        }

        internalSendToReceiver(sender, get());
        return this;
    }

    @Override
    public GameMessage send(Object sender, String permissions) {
        if (sender == null) {
            return this;
        }

        if (sender instanceof Collection) {
            for (Object recipient : (Collection<?>) sender) {
                send(recipient, permissions);
            }
            return this;
        }

        try {
            boolean hasPermissions = (boolean) getMethod(sender,"hasPermission", String.class).invoke(permissions);
            if (hasPermissions) {
                internalSendToReceiver(sender, get());
            }
        } catch (Throwable ignored) {
        }
        return this;
    }

    @Override
    public GameMessage sendList(Object sender) {
        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                send(recipient);
            }
            return this;
        }

        getList().forEach(message -> internalSendToReceiver(sender, message));
        return this;
    }

    @Override
    public GameMessage sendActionBar(Object sender) {
        if (sender instanceof Collection) {
            for (var rec : (Collection<?>) sender) {
                sendActionBar(rec);
            }
            return this;
        }

        try {
            getMethod(sender, "sendMessage", ChatMessageType.class, TextComponent.class).invoke(ChatMessageType.ACTION_BAR, new TextComponent(get()));
        } catch (Throwable ignored) {
        }
        return this;
    }
}
