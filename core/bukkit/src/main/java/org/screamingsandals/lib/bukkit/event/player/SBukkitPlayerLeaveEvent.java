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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerLeaveEvent implements SPlayerLeaveEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerQuitEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    @Nullable
    public Component getLeaveMessage() {
        return ComponentObjectLink.processGetter(event, "quitMessage", event::getQuitMessage);
    }

    @Override
    public void setLeaveMessage(@Nullable Component leaveMessage) {
        ComponentObjectLink.processSetter(event, "quitMessage", event::setQuitMessage, leaveMessage);
    }

    @Override
    public void setLeaveMessage(@Nullable ComponentLike leaveMessage) {
        if (leaveMessage instanceof SenderMessage) {
            setLeaveMessage(((SenderMessage) leaveMessage).asComponent(getPlayer()));
        } else {
            setLeaveMessage(leaveMessage != null ? leaveMessage.asComponent() : null);
        }
    }
}
