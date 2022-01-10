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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface SPlayerTeleportEvent extends SPlayerMoveEvent {

    TeleportCause cause();

    //from bukkit
    // TODO: holder
    enum TeleportCause {
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a
         * command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by a player entering a
         * Nether portal
         */
        NETHER_PORTAL,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * portal
         */
        END_PORTAL,
        /**
         * Indicates the teleportation was caused by a player teleporting to a
         * Entity/Player via the spectator menu
         */
        SPECTATE,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * gateway
         */
        END_GATEWAY,
        /**
         * Indicates the teleportation was caused by a player consuming chorus
         * fruit
         */
        CHORUS_FRUIT,
        /**
         * Indicates the teleportation was caused by an event not covered by
         * this enum
         */
        UNKNOWN;

        public static final List<TeleportCause> VALUES = Arrays.asList(values());

        public static Optional<TeleportCause> getByName(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equals(name.toUpperCase(Locale.ROOT)))
                    .findFirst();
        }
    }
}
