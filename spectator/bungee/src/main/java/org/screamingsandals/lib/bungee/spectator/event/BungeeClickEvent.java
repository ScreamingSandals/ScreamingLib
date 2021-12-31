package org.screamingsandals.lib.bungee.spectator.event;

import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeeClickEvent extends BasicWrapper<net.md_5.bungee.api.chat.ClickEvent> implements ClickEvent {
    public BungeeClickEvent(net.md_5.bungee.api.chat.ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Action action() {
        try {
            return Action.valueOf(wrappedObject.getAction().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // ig COPY_TO_CLIPBOARD have been used
        }
    }

    @Override
    public String value() {
        return wrappedObject.getValue();
    }
}
