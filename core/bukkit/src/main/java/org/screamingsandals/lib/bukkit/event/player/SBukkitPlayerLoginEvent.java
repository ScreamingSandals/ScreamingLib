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

import lombok.*;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.PlayerLoginEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.SPlayerLoginEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

import java.net.InetAddress;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerLoginEvent implements SPlayerLoginEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerLoginEvent event;

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
    public InetAddress getAddress() {
        return event.getAddress();
    }

    @Override
    public String getHostname() {
        return event.getHostname();
    }

    @Override
    public SAsyncPlayerPreLoginEvent.Result getResult() {
        return SAsyncPlayerPreLoginEvent.Result.valueOf(event.getResult().name());
    }

    @Override
    public void setResult(SAsyncPlayerPreLoginEvent.Result result) {
        event.setResult(PlayerLoginEvent.Result.valueOf(result.name()));
    }

    @Override
    public Component getMessage() {
        return ComponentObjectLink.processGetter(event, "kickMessage", event::getKickMessage);
    }

    @Override
    public void setMessage(Component message) {
        ComponentObjectLink.processSetter(event, "kickMessage", event::setKickMessage, message);
    }

    @Override
    public void setMessage(ComponentLike message) {
        if (message instanceof SenderMessage) {
            setMessage(((SenderMessage) message).asComponent(getPlayer()));
        } else {
            setMessage(message.asComponent());
        }
    }
}
