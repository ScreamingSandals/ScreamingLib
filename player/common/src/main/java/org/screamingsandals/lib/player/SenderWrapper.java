package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;

@Data
@RequiredArgsConstructor
public class SenderWrapper implements Wrapper {
    private final String name;
    private final Type type;

    public void sendMessage(String message) {
        PlayerMapper.sendMessage(this, message);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PlayerMapper.convertSenderWrapper(this, type);
    }

    public enum Type {
        PLAYER,
        CONSOLE
    }
}
