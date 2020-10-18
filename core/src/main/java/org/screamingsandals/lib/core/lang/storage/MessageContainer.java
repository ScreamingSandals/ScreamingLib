package org.screamingsandals.lib.core.lang.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class MessageContainer {
    private String text = "";
    private List<Event> events = new LinkedList<>();
    private Map<String, Object> placeholders = new HashMap<>();

    public MessageContainer(String text) {
        this.text = text;
    }

    public static MessageContainer fromComponent(TextComponent component) {
        final var container = new MessageContainer();
        container.setText(component.toLegacyText());

        final var clickEvent = component.getClickEvent();
        final var hoverEvent = component.getHoverEvent();

        if (clickEvent != null) {
            container.events.add(new Event(Event.Action.CLICK, clickEvent.getAction().toString(), clickEvent.getValue()));
        }

        if (hoverEvent != null) {
            container.events.add(new Event(Event.Action.HOVER, hoverEvent.getAction().toString(), TextComponent.toLegacyText(hoverEvent.getValue())));
        }

        return container;
    }

    public TextComponent toComponent() {
        final var component = new TextComponent(TextComponent.fromLegacyText(text));

        if (events.isEmpty()) {
            return component;
        }

        events.forEach(event -> {
            try {
                switch (event.getAction()) {
                    case CLICK:
                        final var clickAction = ClickEvent.Action.valueOf(event.getActionType().toUpperCase());
                        component.setClickEvent(new ClickEvent(clickAction, event.getActionValue()));
                        break;
                    case HOVER:
                        final var hoverAction = HoverEvent.Action.valueOf(event.getActionType().toUpperCase());
                        component.setHoverEvent(new HoverEvent(hoverAction, TextComponent.fromLegacyText(event.getActionValue())));
                        break;
                }
            } catch (Exception e) {
                //TODO: remove after debug or print better one
                e.printStackTrace();
            }
        });
        return component;
    }

    public MessageContainer addPlaceholders(Map<String, Object> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ConfigSerializable
    public static class Event {
        private Action action;
        private String actionType;
        private String actionValue;

        public enum Action {
            HOVER,
            CLICK
        }
    }
}
