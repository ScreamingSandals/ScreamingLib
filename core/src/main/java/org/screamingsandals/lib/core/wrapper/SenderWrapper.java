package org.screamingsandals.lib.core.wrapper;

import com.google.common.base.Preconditions;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public interface SenderWrapper<T> {

    T getInstance();

    String getName();

    void sendMessage(TextComponent message);

    void sendMessage(BaseComponent[] message);

    static SenderWrapper<CommandSender> of(CommandSender instance) {
        Preconditions.checkNotNull(instance, "instance");
        return new BungeeConsoleWrapper(instance);
    }

    static SenderWrapper<org.bukkit.command.CommandSender> of(org.bukkit.command.CommandSender instance) {
        Preconditions.checkNotNull(instance, "instance");
        return new PaperConsoleWrapper(instance);
    }

    default boolean isPlayer() {
        return (this instanceof PaperPlayerWrapper)
                || (this instanceof BungeePlayerWrapper);
    }

    default boolean isConsole() {
        return !isPlayer();
    }
}
