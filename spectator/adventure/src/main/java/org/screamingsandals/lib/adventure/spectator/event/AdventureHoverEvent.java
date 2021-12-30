package org.screamingsandals.lib.adventure.spectator.event;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureEntityContent;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureItemContent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Objects;

public class AdventureHoverEvent extends BasicWrapper<net.kyori.adventure.text.event.HoverEvent<?>> implements HoverEvent {
    public AdventureHoverEvent(net.kyori.adventure.text.event.HoverEvent<?> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Action action() {
        try {
            return Action.valueOf(Objects.requireNonNull(net.kyori.adventure.text.event.HoverEvent.Action.NAMES.key(wrappedObject.action())).toUpperCase());
        } catch (Throwable ignored) {
            return Action.SHOW_TEXT; // HOW??
        }
    }

    @Override
    public Content content() {
        if (wrappedObject.action().type() == Component.class) {
            return AdventureBackend.wrapComponent((Component) wrappedObject.value());
        } else if (wrappedObject.action().type() == net.kyori.adventure.text.event.HoverEvent.ShowItem.class) {
            return new AdventureItemContent((net.kyori.adventure.text.event.HoverEvent.ShowItem) wrappedObject.value());
        } else if (wrappedObject.action().type() == net.kyori.adventure.text.event.HoverEvent.ShowEntity.class) {
            return new AdventureEntityContent((net.kyori.adventure.text.event.HoverEvent.ShowEntity) wrappedObject.value());
        }
        return org.screamingsandals.lib.spectator.Component.empty(); // what?
    }
}
