package org.screamingsandals.lib.velocity.proxiedplayer.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.proxiedplayer.PendingConnection;
import org.screamingsandals.lib.proxiedplayer.event.SPlayerLoginEvent;
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
        if (wrappedEvent.isCancelled()) {
            event.setResult(ResultedEvent.ComponentResult.denied(wrappedEvent.getCancelMessage()));
        }
    }
}
