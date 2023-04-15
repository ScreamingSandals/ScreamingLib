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
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.spectator.event.hover.*;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;

public interface HoverEvent extends Wrapper, RawValueHolder {

    @Contract(value = "-> new", pure = true)
    static HoverEvent.@NotNull Builder builder() {
        return Spectator.getBackend().hoverEvent();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showText(@NotNull Component text) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_TEXT).content(text).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showText(@NotNull ComponentLike text) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_TEXT).content(text).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showItem(@NotNull ItemContent itemContent) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_ITEM).content(itemContent).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showItem(@NotNull ItemContentLike itemContent) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_ITEM).content(itemContent).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showEntity(@NotNull EntityContent itemContent) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_ENTITY).content(itemContent).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull HoverEvent showEntity(@NotNull EntityContentLike itemContent) {
        return Spectator.getBackend().hoverEvent().action(Action.SHOW_ENTITY).content(itemContent).build();
    }

    @NotNull Action action();

    @NotNull Content content();

    enum Action {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY
        /* SHOW_ACHIEVEMENT no longer exist */
    }

    interface Builder {
        @Contract("_ -> this")
        @NotNull Builder action(@NotNull Action action);

        @Contract("_ -> this")
        @NotNull Builder content(@NotNull Content content);

        @Contract("_ -> this")
        default @NotNull Builder content(@NotNull ContentLike contentLike) {
            return content(contentLike.asContent());
        }

        @Contract(value = "-> new", pure = true)
        @NotNull HoverEvent build();
    }
}
