package org.screamingsandals.lib.core.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PaperConsoleWrapper implements SenderWrapper<CommandSender> {
    @Getter
    private final CommandSender instance;

    @Override
    public String getName() {
        return instance.getName();
    }

    @Override
    public void sendMessage(TextComponent message) {
        instance.sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent[] message) {
        instance.sendMessage(message);
    }
}
