package org.screamingsandals.lib.proxiedplayer;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ProxiedPlayerWrapper {
    private final String name;
    private final UUID uuid;

    public void sendMessage(String message) {
        ProxiedPlayerUtils.sendMessage(this, message);
    }

    public void switchServer(ServerWrapper server) {
        ProxiedPlayerUtils.switchServer(this, server);
    }

    public <T> T as(Class<T> type) {
        return ProxiedPlayerUtils.convertPlayerWrapper(this, type);
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
