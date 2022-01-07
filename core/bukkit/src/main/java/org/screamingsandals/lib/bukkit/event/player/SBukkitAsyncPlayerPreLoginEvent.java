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
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

import java.net.InetAddress;
import java.util.UUID;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitAsyncPlayerPreLoginEvent implements SAsyncPlayerPreLoginEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final AsyncPlayerPreLoginEvent event;

    @Override
    public UUID getUuid() {
        return event.getUniqueId();
    }

    @Override
    public InetAddress getAddress() {
        return event.getAddress();
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public Result getResult() {
        return Result.valueOf(event.getLoginResult().name());
    }

    @Override
    public void setResult(Result result) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.valueOf(result.name()));
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
        setMessage(message.asComponent());
    }
}
