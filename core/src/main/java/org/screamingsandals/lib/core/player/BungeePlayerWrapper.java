package org.screamingsandals.lib.core.player;

import lombok.Data;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.UUID;

@Data
public class BungeePlayerWrapper implements PlayerWrapper<ProxiedPlayer> {
    private final transient ProxiedPlayer instance;

    @Override
    public String getName() {
        return instance.getName();
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public String getAddress() {
        final var address = (InetSocketAddress) instance.getSocketAddress();
        return address.getAddress().getHostAddress();
    }

    @Override
    public void kick(TextComponent reason) {
        instance.disconnect(reason);
    }

    @Override
    public void sendMessage(TextComponent message) {
        instance.sendMessage(message);
    }
}
