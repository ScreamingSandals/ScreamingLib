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

package org.screamingsandals.lib.proxy.event;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.Event;
import org.screamingsandals.lib.proxy.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public interface PlayerLeaveEvent extends Event {
    @NotNull LoginStatus getStatus();

    @NotNull ProxiedPlayer getPlayer();

    enum LoginStatus {
        CANCELLED_BY_PROXY,
        CANCELLED_BY_USER,
        CANCELLED_BY_USER_BEFORE_COMPLETE,
        CONFLICTING_LOGIN,
        PRE_SERVER_JOIN,
        SUCCESSFUL_LOGIN;

        public static final @NotNull List<@NotNull LoginStatus> VALUES = Arrays.asList(values());

        public static @NotNull LoginStatus convert(@NotNull String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow();
        }
    }
}
