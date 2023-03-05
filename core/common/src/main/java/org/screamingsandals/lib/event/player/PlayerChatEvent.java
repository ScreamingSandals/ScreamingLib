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
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.Player;

import java.util.Collection;

public interface PlayerChatEvent extends SCancellableEvent, PlayerEvent, PlatformEvent {
    @NotNull Collection<@NotNull Player> recipients();

    @NotNull Player sender();

    @NotNull String message();

    void message(@NotNull String message);

    @NotNull String format();

    void format(@NotNull String format);

    @Override
    default @NotNull Player player() {
        return sender();
    }
}
