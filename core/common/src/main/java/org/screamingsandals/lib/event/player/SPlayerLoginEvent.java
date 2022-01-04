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

package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.net.InetAddress;

public interface SPlayerLoginEvent extends SPlayerEvent, PlatformEventWrapper {

    PlayerWrapper getPlayer();

    InetAddress getAddress();

    String getHostname();

    SAsyncPlayerPreLoginEvent.Result getResult();

    void setResult(SAsyncPlayerPreLoginEvent.Result result);

    Component getMessage();

    void setMessage(Component message);

    void setMessage(ComponentLike message);

    /**
     * Allows the player to log in
     */
    default void allow() {
        setResult(SAsyncPlayerPreLoginEvent.Result.ALLOWED);
        setMessage(Component.empty());
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    default void disallow(@NotNull final SAsyncPlayerPreLoginEvent.Result result, @NotNull final Component message) {
        setResult(result);
        setMessage(message);
    }
}
