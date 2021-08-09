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
