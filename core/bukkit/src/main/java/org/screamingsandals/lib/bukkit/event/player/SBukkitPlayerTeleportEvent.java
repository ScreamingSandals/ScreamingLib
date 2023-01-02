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

package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.player.SPlayerTeleportEvent;

public class SBukkitPlayerTeleportEvent extends SBukkitPlayerMoveEvent implements SPlayerTeleportEvent {
    public SBukkitPlayerTeleportEvent(PlayerTeleportEvent event) {
        super(event);
    }

    // Internal cache
    private TeleportCause teleportCause;

    @Override
    public @NotNull TeleportCause cause() {
        if (teleportCause == null) {
            teleportCause = TeleportCause.valueOf(event().getCause().name());
        }
        return teleportCause;
    }

    @Override
    public @NotNull PlayerTeleportEvent event() {
        return (PlayerTeleportEvent) super.event();
    }
}
