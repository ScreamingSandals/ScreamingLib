package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;

@Data
@RequiredArgsConstructor
public class SenderWrapper implements Wrapper {
    private final String name;

    public void sendMessage(String message) {
        PlayerUtils.sendMessage(this, message);
    }

    @Override
    public <T> T as(Class<T> type) {
        return null;
    }
}
