package org.screamingsandals.lib.core.wrapper.sender;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

@RequiredArgsConstructor
@Getter
public abstract class AbstractSender<T> implements SenderWrapper<T> {
    protected final T instance;
    protected final Audience audience;

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(Component message) {
        audience.sendMessage(message);
    }

    @Override
    public void sendMessage(Identity identity, Component message) {
        audience.sendMessage(identity, message);
    }

    @Override
    public Audience getAudience() {
        return audience;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
