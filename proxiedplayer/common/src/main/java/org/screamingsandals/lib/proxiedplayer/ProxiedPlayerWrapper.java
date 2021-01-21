package org.screamingsandals.lib.proxiedplayer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ProxiedPlayerWrapper implements Wrapper {
    private final String name;
    private final UUID uuid;

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
