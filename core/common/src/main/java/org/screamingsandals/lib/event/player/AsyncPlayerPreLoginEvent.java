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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.SAsyncEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.net.InetAddress;
import java.util.UUID;

public interface AsyncPlayerPreLoginEvent extends SAsyncEvent, PlatformEvent {

    /**
     * Gets the UUID
     *
     * @return {@link UUID} of the player.
     */
    @NotNull UUID uuid();

    /**
     * Gets the address
     *
     * @return {@link InetAddress} of the player.
     */
    @NotNull InetAddress address();

    /**
     * Gets the name
     *
     * @return name of the player
     */
    @NotNull String name();

    /**
     * Sets the new name of the player
     *
     * @param name name to set
     */
    default void name(@NotNull String name) {
        throw new UnsupportedOperationException("Name is not changeable on this platform!");
    }

    /**
     * Gets the result of the event
     *
     * @return {@link Result}
     */
    @NotNull Result result();

    /**
     * Sets new result for this event
     *
     * @param result new result
     */
    void result(@NotNull Result result);

    /**
     * Gets the message that is displayed if the {@link Result} is not {@link Result#ALLOWED}.
     *
     * @return {@link Component}
     */
    @NotNull Component message();

    /**
     * Sets new message that is displayed if the {@link Result} is not {@link Result#ALLOWED}.
     *
     * @param message new message
     */
    void message(@NotNull Component message);

    /**
     * Sets new message that is displayed if the {@link Result} is not {@link Result#ALLOWED}.
     *
     * @param message new message
     */
    void message(@NotNull ComponentLike message);

    /**
     * Result of the join event.
     * <p>
     * NOTE: from Paper.
     */
    enum Result {
        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the
         * white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER
    }
}
