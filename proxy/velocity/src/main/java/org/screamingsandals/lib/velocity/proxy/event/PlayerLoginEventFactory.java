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

package org.screamingsandals.lib.velocity.proxy.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxy.PendingConnection;
import org.screamingsandals.lib.proxy.event.SPlayerLoginEvent;
import org.screamingsandals.lib.velocity.event.AbstractVelocityEventHandlerFactory;

public class PlayerLoginEventFactory extends AbstractVelocityEventHandlerFactory<LoginEvent, SPlayerLoginEvent> {

    public PlayerLoginEventFactory(Object plugin, ProxyServer proxyServer) {
        super(LoginEvent.class, SPlayerLoginEvent.class, plugin, proxyServer, true);
    }

    @Override
    protected SPlayerLoginEvent wrapEvent(LoginEvent event, EventPriority priority) {
        final var player = event.getPlayer();
        final var connection = new PendingConnection(player.getUsername(),
                player.getProtocolVersion().getProtocol(),
                player.getRemoteAddress(), false,
                player.getUniqueId(), player.isOnlineMode());
        return new SPlayerLoginEvent(connection);
    }

    @Override
    protected void postProcess(SPlayerLoginEvent wrappedEvent, LoginEvent event) {
        if (wrappedEvent.cancelled()) {
            event.setResult(ResultedEvent.ComponentResult.denied(wrappedEvent.getCancelMessage().as(Component.class)));
        }
    }
}
