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

package org.screamingsandals.lib.velocity.proxy.event;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxy.ProxiedPlayerMapper;
import org.screamingsandals.lib.proxy.event.SPlayerLeaveEvent;
import org.screamingsandals.lib.velocity.event.AbstractVelocityEventHandlerFactory;

public class PlayerLeaveEventFactory extends AbstractVelocityEventHandlerFactory<DisconnectEvent, SPlayerLeaveEvent> {

    public PlayerLeaveEventFactory(Object plugin, ProxyServer proxyServer) {
        super(DisconnectEvent.class, SPlayerLeaveEvent.class, plugin, proxyServer);
    }

    @Override
    protected SPlayerLeaveEvent wrapEvent(DisconnectEvent event, EventPriority priority) {
        final var player = event.getPlayer();
        final var login = SPlayerLeaveEvent.LoginStatus.convert(event.getLoginStatus().name());
        return new SPlayerLeaveEvent(ProxiedPlayerMapper.wrapPlayer(player), login);
    }

    @Override
    protected void postProcess(SPlayerLeaveEvent wrappedEvent, DisconnectEvent event) {

    }
}
