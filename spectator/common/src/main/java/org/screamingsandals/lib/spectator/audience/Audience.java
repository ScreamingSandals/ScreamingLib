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

package org.screamingsandals.lib.spectator.audience;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;

public interface Audience {

    void sendMessage(@NotNull ComponentLike message);

    default void sendRichMessage(@NotNull String message) {
        sendMessage(Component.fromMiniMessage(message));
    }

    default void sendPlainMessage(@NotNull String message) {
        sendMessage(Component.text(message));
    }

    default void sendPlainMessage(@NotNull String message, @NotNull Color color) {
        sendMessage(Component.text(message, color));
    }

    @FunctionalInterface
    interface ForwardingToMulti extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Iterable<? extends Audience> audiences();

        @Override
        default void sendMessage(@NotNull ComponentLike message) {
            audiences().forEach(audience -> audience.sendMessage(message));
        }
    }

    @FunctionalInterface
    interface ForwardingToSingle extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Audience audience();

        @Override
        default void sendMessage(@NotNull ComponentLike message) {
            audience().sendMessage(message);
        }
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable // don't extend it guys
    interface ForwardingToAdapter extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Adapter adapter();

        @Override
        default void sendMessage(@NotNull ComponentLike message) {
            adapter().sendMessage(message);
        }
    }
}
