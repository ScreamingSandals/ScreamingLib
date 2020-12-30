package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PlayerWrapper {
    private final String name;
    private final UUID uuid;

    public void sendMessage(String message) {
        PlayerUtils.sendMessage(this, message);
    }

    public <T> T as(Class<T> type) {
        return PlayerUtils.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((PlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
