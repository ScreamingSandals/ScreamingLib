/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.net.InetAddress;

public interface PlayerLoginEvent extends PlayerEvent, PlatformEvent {

    @NotNull Player player();

    @NotNull InetAddress address();

    @NotNull String hostname();

    AsyncPlayerPreLoginEvent.@NotNull Result result();

    void result(AsyncPlayerPreLoginEvent.@NotNull Result result);

    @NotNull Component message();

    void message(@NotNull Component message);

    void message(@NotNull ComponentLike message);

    /**
     * Allows the player to log in
     */
    default void allow() {
        result(AsyncPlayerPreLoginEvent.Result.ALLOWED);
        message(Component.empty());
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    default void disallow(final AsyncPlayerPreLoginEvent.@NotNull Result result, final @NotNull Component message) {
        result(result);
        message(message);
    }
}
