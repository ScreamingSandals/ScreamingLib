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
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SAsyncEvent;

import java.net.InetAddress;
import java.util.UUID;

public interface SAsyncPlayerPreLoginEvent extends SAsyncEvent, PlatformEventWrapper {

    UUID getUuid();

    InetAddress getAddress();

    String getName();

    default void setName(String name) {
        throw new UnsupportedOperationException("Name is not changeable on this platform!");
    }

    Result getResult();

    void setResult(Result result);

    Component getMessage();

    void setMessage(Component message);

    void setMessage(ComponentLike message);

    //from paper
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
        KICK_OTHER;
    }
}
