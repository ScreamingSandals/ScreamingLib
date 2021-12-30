package org.screamingsandals.lib.adventure.spectator.event;

import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.utils.BasicWrapper;

public class AdventureClickEvent extends BasicWrapper<net.kyori.adventure.text.event.ClickEvent> implements ClickEvent {
    public AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Action action() {
        try {
            return Action.valueOf(wrappedObject.action().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // HOW??
        }
    }

    @Override
    public String value() {
        return wrappedObject.value();
    }
}
