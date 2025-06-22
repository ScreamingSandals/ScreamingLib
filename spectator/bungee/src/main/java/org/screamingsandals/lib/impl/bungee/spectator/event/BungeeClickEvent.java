/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.ClickEventCustom;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.impl.bungee.spectator.BungeeChatFeature;
import org.screamingsandals.lib.impl.bungee.spectator.dialog.BungeeDialog;
import org.screamingsandals.lib.impl.bungee.spectator.dialog.BungeeDialogReference;
import org.screamingsandals.lib.impl.bungee.spectator.event.click.BungeeClickEventCustom;
import org.screamingsandals.lib.impl.bungee.spectator.event.click.BungeeShowDialogClickEvent;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

public class BungeeClickEvent extends BasicWrapper<net.md_5.bungee.api.chat.ClickEvent> implements ClickEvent {
    public BungeeClickEvent(net.md_5.bungee.api.chat.@NotNull ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Action action() {
        try {
            return Action.valueOf(wrappedObject.getAction().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // ig COPY_TO_CLIPBOARD have been used
        }
    }

    @Override
    public @NotNull ClickEvent withAction(@NotNull Action action) {
        net.md_5.bungee.api.chat.ClickEvent.Action bungeeAction;
        try {
            bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(action.name());
        } catch (Throwable throwable) {
            bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
        }
        if (BungeeChatFeature.NEW_CLICK_EVENTS.isSupported()) {
            if (action == Action.CUSTOM) {
                if (wrappedObject instanceof ClickEventCustom) {
                    return this;
                } else {
                    return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEventCustom(wrappedObject.getValue(), null));
                }
            } else if (action == Action.SHOW_DIALOG) {
                if (wrappedObject instanceof ShowDialogClickEvent) {
                    return this;
                } else {
                    return new BungeeClickEvent(new ShowDialogClickEvent(wrappedObject.getValue()));
                }
            }
        }
        return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(bungeeAction, wrappedObject.getValue()));
    }

    @Override
    public @NotNull Payload payload() {
        if (BungeeChatFeature.NEW_CLICK_EVENTS.isSupported()) {
            if (wrappedObject instanceof ClickEventCustom) {
                return new BungeeClickEventCustom((ClickEventCustom) wrappedObject);
            } else if (wrappedObject instanceof ShowDialogClickEvent) {
                return new BungeeShowDialogClickEvent((ShowDialogClickEvent) wrappedObject);
            }
        }
        var text = wrappedObject.getValue();
        return Payload.text(text != null ? text : "");
    }

    @Override
    public @NotNull ClickEvent withPayload(@NotNull Payload payload) {
        if (BungeeChatFeature.NEW_CLICK_EVENTS.isSupported()) {
            if (action() == Action.CUSTOM) {
                if (payload instanceof BungeeClickEventCustom) {
                    return new BungeeClickEvent(((BungeeClickEventCustom) payload).as(ClickEventCustom.class));
                } else if (payload instanceof Payload.Custom) {
                    return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEventCustom(
                            ((Payload.Custom) payload).location().toString(),
                            AbstractBungeeBackend.getSnbtSerializer().serialize(((Payload.Custom) payload).tag())
                    ));
                }
                throw new IllegalArgumentException("Invalid payload type for action " + wrappedObject.getAction().name() + ": " + payload.getClass());
            } else if (action() == Action.SHOW_DIALOG) {
                if (payload instanceof BungeeShowDialogClickEvent) {
                    return new BungeeClickEvent(((BungeeShowDialogClickEvent) payload).as(ShowDialogClickEvent.class));
                } else if (payload instanceof Payload.ShowDialog) {
                    var dialog = ((Payload.ShowDialog) payload).dialog();
                    if (dialog instanceof BungeeDialog) {
                        return new BungeeClickEvent(new ShowDialogClickEvent(dialog.as(Dialog.class)));
                    } else if (dialog instanceof BungeeDialogReference) {
                        return new BungeeClickEvent(new ShowDialogClickEvent(((BungeeDialogReference) dialog).getReference()));
                    }
                }
                throw new IllegalArgumentException("Invalid payload type for action " + wrappedObject.getAction().name() + ": " + payload.getClass());
            }
        }
        String value;
        if (payload instanceof Payload.Text) {
            value = ((Payload.Text) payload).text();
        } else if (payload instanceof Payload.Int) {
            value = String.valueOf(((Payload.Int) payload).number());
        } else {
            throw new IllegalArgumentException("Invalid payload type for action " + wrappedObject.getAction().name() + ": " + payload.getClass());
        }
        return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(wrappedObject.getAction(), value));
    }

    @Override
    public ClickEvent.@NotNull Builder toBuilder() {
        return new BungeeClickBuilder(action(), payload());
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalClickEventConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeClickBuilder implements ClickEvent.Builder {
        private @NotNull Action action = Action.OPEN_URL;
        private @Nullable Payload payload;

        @Override
        public @NotNull ClickEvent build() {
            Preconditions.checkNotNull(action, "Action is not specified!");
            Preconditions.checkNotNull(payload, "Payload is not specified!");
            net.md_5.bungee.api.chat.ClickEvent.Action action;
            try {
                action = net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(this.action.name());
            } catch (Throwable throwable) {
                action = net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
            }
            if (BungeeChatFeature.NEW_CLICK_EVENTS.isSupported()) {
                if (this.action == Action.CUSTOM) {
                    if (payload instanceof BungeeClickEventCustom) {
                        return new BungeeClickEvent(((BungeeClickEventCustom) payload).as(ClickEventCustom.class));
                    } else if (payload instanceof Payload.Custom) {
                        return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEventCustom(
                                ((Payload.Custom) payload).location().toString(),
                                AbstractBungeeBackend.getSnbtSerializer().serialize(((Payload.Custom) payload).tag())
                        ));
                    }
                    throw new IllegalArgumentException("Invalid payload type for action " + action.name() + ": " + payload.getClass());
                } else if (this.action == Action.SHOW_DIALOG) {
                    if (payload instanceof BungeeShowDialogClickEvent) {
                        return new BungeeClickEvent(((BungeeShowDialogClickEvent) payload).as(ShowDialogClickEvent.class));
                    } else if (payload instanceof Payload.ShowDialog) {
                        var dialog = ((Payload.ShowDialog) payload).dialog();
                        if (dialog instanceof BungeeDialog) {
                            return new BungeeClickEvent(new ShowDialogClickEvent(dialog.as(Dialog.class)));
                        } else if (dialog instanceof BungeeDialogReference) {
                            return new BungeeClickEvent(new ShowDialogClickEvent(((BungeeDialogReference) dialog).getReference()));
                        }
                    }
                    throw new IllegalArgumentException("Invalid payload type for action " +action.name() + ": " + payload.getClass());
                }
            }
            String value;
            if (payload instanceof Payload.Text) {
                value = ((Payload.Text) payload).text();
            } else if (payload instanceof Payload.Int) {
                value = String.valueOf(((Payload.Int) payload).number());
            } else {
                throw new IllegalArgumentException("Invalid payload type for action " + action.name() + ": " + payload.getClass());
            }
            return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(action, value));
        }
    }
}
