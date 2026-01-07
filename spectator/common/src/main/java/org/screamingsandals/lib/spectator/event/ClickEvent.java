/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.spectator.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.spectator.dialog.Dialog;
import org.screamingsandals.lib.spectator.event.click.Payload;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface ClickEvent extends Wrapper, RawValueHolder {

    @Contract(value = "-> new", pure = true)
    static ClickEvent.@NotNull Builder builder() {
        return Spectator.getBackend().clickEvent();
    }

    @LimitedVersionSupport(">= 1.15")
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent copyToClipboard(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.COPY_TO_CLIPBOARD).payload(Payload.text(value)).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent openUrl(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.OPEN_URL).payload(Payload.text(value)).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent openFile(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.OPEN_FILE).payload(Payload.text(value)).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent runCommand(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.RUN_COMMAND).payload(Payload.text(value)).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent suggestCommand(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.SUGGEST_COMMAND).payload(Payload.text(value)).build();
    }

    /**
     * @see #changePage(int)
     */
    @Deprecated(forRemoval = true)
    @LimitedVersionSupport(">= 1.15")
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent changePage(@NotNull String value) {
        try {
            return changePage(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot set value " + value + " as payload for CHANGE_PAGE click event", e);
        }
    }

    @Contract(value = "_ -> new", pure = true)
    @LimitedVersionSupport(">= 1.15")
    static @NotNull ClickEvent changePage(int page) {
        return Spectator.getBackend().clickEvent().action(Action.CHANGE_PAGE).payload(Payload.integer(page)).build();
    }

    @Contract(value = "_, _ -> new", pure = true)
    @LimitedVersionSupport(">= 1.21.6")
    static @NotNull ClickEvent custom(@NotNull ResourceLocation id, @Nullable Tag data) {
        return Spectator.getBackend().clickEvent().action(Action.CUSTOM).payload(Payload.custom(id, data)).build();
    }

    @Contract(value = "_ -> new", pure = true)
    @LimitedVersionSupport(">= 1.21.6")
    static @NotNull ClickEvent showDialog(@NotNull Dialog dialog) {
        return Spectator.getBackend().clickEvent().action(Action.SHOW_DIALOG).payload(Payload.showDialog(dialog)).build();
    }

    @NotNull Action action();

    @Contract(pure = true)
    @NotNull ClickEvent withAction(@NotNull Action action);

    /**
     * @see #payload()
     */
    @Deprecated(forRemoval = true)
    default @NotNull String value() {
        var payload = payload();
        if (payload instanceof Payload.Text) {
            return ((Payload.Text) payload).text();
        } else if (payload instanceof Payload.Int) {
            return String.valueOf(((Payload.Int) payload).number());
        } else {
            throw new IllegalStateException("Payload is not a string payload, is " + payload);
        }
    }

    /**
     * @see #withPayload(Payload)
     */
    @Deprecated(forRemoval = true)
    @Contract(pure = true)
    default @NotNull ClickEvent withValue(@NotNull String value) {
        return withPayload(Payload.text(value));
    }

    @NotNull Payload payload();

    /**
     * @throws IllegalArgumentException if the payload is not supported by the action
     */
    @Contract(pure = true)
    @NotNull ClickEvent withPayload(@NotNull Payload payload);

    @Contract(value = "-> new", pure = true)
    ClickEvent.@NotNull Builder toBuilder();

    enum Action {
        OPEN_URL,
        OPEN_FILE,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        /**
         * Books only
         */
        CHANGE_PAGE,
        @LimitedVersionSupport(">= 1.15")
        COPY_TO_CLIPBOARD,
        @LimitedVersionSupport(">= 1.21.6")
        SHOW_DIALOG,
        @LimitedVersionSupport(">= 1.21.6")
        CUSTOM;

        public @NotNull Class<? extends Payload> supportedPayload() {
            switch (this) {
                case CHANGE_PAGE:
                    return Payload.Int.class;
                case SHOW_DIALOG:
                    return Payload.ShowDialog.class;
                case CUSTOM:
                    return Payload.Custom.class;
                default:
                    return Payload.Text.class;
            }
        }

        public boolean supportsPayload(@NotNull Class<? extends Payload> payloadClass) {
            return supportedPayload().isAssignableFrom(payloadClass) || (this == CHANGE_PAGE && Payload.Text.class.isAssignableFrom(payloadClass));
        }
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder action(@NotNull Action action);

        /**
         * @see #payload(Payload)
         */
        @Deprecated(forRemoval = true)
        @Contract("_ -> this")
        default @NotNull Builder value(@NotNull String value) {
            return payload(Payload.text(value));
        }

        @Contract("_ -> this")
        @NotNull Builder payload(@NotNull Payload payload);

        @Contract(value = "-> new", pure = true)
        @NotNull ClickEvent build();
    }
}
