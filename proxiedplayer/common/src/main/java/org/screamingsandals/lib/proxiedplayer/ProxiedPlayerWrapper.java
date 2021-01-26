package org.screamingsandals.lib.proxiedplayer;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProxiedPlayerWrapper extends ProxiedSenderWrapper {
    private final UUID uuid;

    public ProxiedPlayerWrapper(String name, UUID uuid) {
        super(name, Type.PLAYER);
        this.uuid = uuid;
    }

    public void sendMessage(String message) {
        ProxiedPlayerMapper.sendMessage(this, message);
    }

    public void switchServer(ServerWrapper server) {
        ProxiedPlayerMapper.switchServer(this, server);
    }

    public <T> T as(Class<T> type) {
        return ProxiedPlayerMapper.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProxiedPlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((ProxiedPlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
