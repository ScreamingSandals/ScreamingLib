package org.screamingsandals.lib.core.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BungeeConsoleWrapper implements SenderWrapper<CommandSender> {
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
}
