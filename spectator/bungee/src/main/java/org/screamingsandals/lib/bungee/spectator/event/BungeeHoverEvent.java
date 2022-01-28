/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bungee.spectator.event;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
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
                            return AbstractBungeeBackend.wrapComponent(new TextComponent((BaseComponent[]) text.getValue()));
                        } else if (text.getValue() instanceof BaseComponent) {
                            return AbstractBungeeBackend.wrapComponent((BaseComponent) text.getValue());
                        } else {
                            return AbstractBungeeBackend.wrapComponent(new TextComponent(text.getValue().toString()));
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
                        return AbstractBungeeBackend.wrapComponent(new TextComponent(components.toArray(BaseComponent[]::new)));
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
                        return AbstractBungeeBackend.wrapComponent(values[0]);
                    } else {
                        return AbstractBungeeBackend.wrapComponent(new TextComponent(values[0]));
                    }

            }
        }
    }
}
