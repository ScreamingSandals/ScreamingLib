package org.screamingsandals.lib.bungee.spectator.event;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.screamingsandals.lib.bungee.spectator.BungeeBackend;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeEntityContent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeItemContent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.ArrayList;

public class BungeeHoverEvent extends BasicWrapper<net.md_5.bungee.api.chat.HoverEvent> implements HoverEvent {
    public BungeeHoverEvent(net.md_5.bungee.api.chat.HoverEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Action action() {
        try {
            return Action.valueOf(wrappedObject.getAction().name());
        } catch (Throwable ignored) {
            return Action.SHOW_TEXT; // SHOW_ACHIEVEMENT ig
        }
    }

    @Override
    public Content content() {
        try {
            // new api
            var content = wrappedObject.getContents();
            switch (wrappedObject.getAction()) {
                case SHOW_ENTITY:
                    if (content.size() == 0) {
                        return null; // Dear API, this is not valid
                    } else {
                        // Dear API, more values are also not valid
                        return new BungeeEntityContent((Entity) content.get(0));
                    }
                case SHOW_ITEM:
                    if (content.size() == 0) {
                        return null; // Dear API, this is not valid
                    } else {
                        // Dear API, more values are also not valid
                        return new BungeeItemContent((Item) content.get(0));
                    }
                default:
                    if (content.size() == 0) {
                        return Component.empty();
                    } else if (content.size() == 1) {
                        var text = (Text) content.get(0);
                        if (text.getValue() instanceof BaseComponent[]) {
                            return BungeeBackend.wrapComponent(new TextComponent((BaseComponent[]) text.getValue()));
                        } else if (text.getValue() instanceof BaseComponent) {
                            return BungeeBackend.wrapComponent((BaseComponent) text.getValue());
                        } else {
                            return BungeeBackend.wrapComponent(new TextComponent(text.getValue().toString()));
                        }
                    } else {
                        var components = new ArrayList<BaseComponent>();
                        for (var textC : content) {
                            var text = (Text) textC;
                            if (text.getValue() instanceof BaseComponent[]) {
                                components.add(new TextComponent((BaseComponent[]) text.getValue()));
                            } else if (text.getValue() instanceof BaseComponent) {
                                components.add(((BaseComponent) text.getValue()));
                            } else {
                                components.add(new TextComponent(text.getValue().toString()));
                            }
                        }
                        return BungeeBackend.wrapComponent(new TextComponent(components.toArray(BaseComponent[]::new)));
                    }
            }
        } catch (Throwable ignored) {
            // old api
            var values = wrappedObject.getValue();
            switch (wrappedObject.getAction()) {
                case SHOW_ENTITY:
                    if (values.length == 1 && values[0] instanceof TextComponent) {
                        var value = ((TextComponent) values[0]).getText();
                        // TODO: basically that fucker wants me to parse SNBT
                        return null;
                    } else {
                        return null; // WTF??
                    }
                case SHOW_ITEM:
                    if (values.length == 1 && values[0] instanceof TextComponent) {
                        var value = ((TextComponent) values[0]).getText();
                        // TODO: basically that fucker wants me to parse SNBT
                        return null;
                    } else {
                        return null; // WTF??
                    }
                default:
                    if (values.length == 0) {
                        return Component.empty();
                    } else if (values.length == 1) {
                        return BungeeBackend.wrapComponent(values[0]);
                    } else {
                        return BungeeBackend.wrapComponent(new TextComponent(values[0]));
                    }

            }
        }
    }
}
