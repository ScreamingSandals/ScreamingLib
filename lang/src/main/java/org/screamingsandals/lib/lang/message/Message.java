package org.screamingsandals.lib.lang.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.lib.lang.Base;
import org.screamingsandals.lib.lang.storage.Storage;

import java.util.*;

public class Message {
    protected String key;
    protected Storage storage;
    protected List<String> defList;
    protected String def;
    protected boolean prefix;
    protected final Map<String, Object> replaces = new HashMap<>();

    public Message(String key, Storage storage) {
        this(key, storage, (String) null, false);
    }

    public Message(String key, Storage storage, boolean prefix) {
        this(key, storage, (String) null, prefix);
    }

    public Message(String key, Storage storage, String def) {
        this(key, storage, def, false);
    }

    public Message(String key, Storage storage, String def, boolean prefix) {
        this.key = key;
        this.storage = storage;
        this.def = def;
        this.prefix = prefix;
    }

    public Message(String key, Storage storage, List<String> defList) {
        this(key, storage, defList, false);
    }

    public Message(String key, Storage storage, List<String> defList, boolean prefix) {
        this.key = key;
        this.storage = storage;
        this.defList = defList;
        this.prefix = prefix;
    }

    public Message replace(String placeholder, Object replacement) {
        replaces.put(placeholder, replacement);
        return this;
    }

    public Message replaceAll(Map<String, Object> replacements) {
        replaces.putAll(replacements);
        return this;
    }

    public Message key(String key) {
        this.key = key;
        return this;
    }

    public Message def(String def) {
        this.def = def;
        return this;
    }

    public Message clearReplaces() {
        this.replaces.clear();
        return this;
    }

    public Message reset(String key) {
        return reset(key, null, false);
    }

    public Message reset(String key, String def) {
        return reset(key, def, false);
    }

    public Message reset(String key, String def, boolean prefix) {
        this.key = key;
        this.def = def;
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

    public Message setStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    public Message setLanguage(Object player) {
        String language = "";

        try {
            language = (String) player.getClass().getMethod("getLocale").invoke(player);
        } catch (Throwable ignored) {
            try {
                language = (String) player.getClass().getMethod("getLanguage").invoke(player);
            } catch (Throwable ignored2) {
            }
        }

        setLanguage(language);
        return this;
    }

    public Message setLanguage(String language) {
        storage = Base.getStorage(language);
        return this;
    }

    public boolean exists() {
        return storage.exists(key);
    }

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

    public List<String> getList() {
        try {
            final List<String> messages = storage.translateList(key, defList, prefix);
            final List<String> toReturn = new ArrayList<>();

            for (String toTranslate : messages) {
                for (var replace : replaces.entrySet()) {
                    System.out.println(replace);
                    toTranslate = toTranslate.replaceAll(replace.getKey(), replace.getValue().toString());
                }
                toReturn.add(toTranslate);
            }

            return toReturn;
        } catch (Throwable ignored) {
            return null;
        }
    }

    public String toString() {
        return get();
    }

    public Message send(Object sender) {
        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                send(recipient);
            }
            return this;
        }

        internalSendToReceiver(sender, get());
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
            boolean hasPermissions = (boolean) sender.getClass().getMethod("hasPermission", String.class).invoke(sender, permissions);
            if (hasPermissions) {
                internalSendToReceiver(sender, get());
            }
        } catch (Throwable ignored) {
        }
        return this;
    }

    public Message sendList(Object sender) {
        if (sender instanceof Collection) {
            for (var recipient : (Collection<?>) sender) {
                sendList(recipient);
            }
            return this;
        }

        getList().forEach(message -> internalSendToReceiver(sender, message));
        return this;
    }

    public Message sendActionBar(Object sender) {
        if (sender instanceof Collection) {
            for (var rec : (Collection<?>) sender) {
                sendActionBar(rec);
            }
            return this;
        }

        try {
            sender.getClass().getMethod("sendMessage", ChatMessageType.class, TextComponent.class).invoke(sender, ChatMessageType.ACTION_BAR, new TextComponent(get()));
        } catch (Throwable ignored) {
        }
        return this;
    }

    protected void internalSendToReceiver(Object sender, String message) {
        if (message.isEmpty()) {
            //TODO: add some debug here?
            return;
        }

        try {
            sender.getClass().getMethod("sendMessage", String.class).invoke(sender, message);
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }
}
