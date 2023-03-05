/*
 * Copyright 2023 ScreamingSandals
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
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface ClickEvent extends Wrapper, RawValueHolder {

    @Contract(value = "-> new", pure = true)
    static ClickEvent.@NotNull Builder builder() {
        return Spectator.getBackend().clickEvent();
    }

    @LimitedVersionSupport(">= 1.15")
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent copyToClipboard(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.COPY_TO_CLIPBOARD).value(value).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent openUrl(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.OPEN_URL).value(value).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent openFile(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.OPEN_FILE).value(value).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent runCommand(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.RUN_COMMAND).value(value).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent suggestCommand(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.SUGGEST_COMMAND).value(value).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ClickEvent changePage(@NotNull String value) {
        return Spectator.getBackend().clickEvent().action(Action.CHANGE_PAGE).value(value).build();
    }

    @NotNull Action action();

    @Contract(pure = true)
    @NotNull ClickEvent withAction(@NotNull Action action);

    @NotNull String value();

    @Contract(pure = true)
    @NotNull ClickEvent withValue(@NotNull String value);

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
        COPY_TO_CLIPBOARD
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder action(@NotNull Action action);

        @Contract("_ -> this")
        @NotNull Builder value(@NotNull String value);

        @Contract(value = "-> new", pure = true)
        @NotNull ClickEvent build();
    }
}
