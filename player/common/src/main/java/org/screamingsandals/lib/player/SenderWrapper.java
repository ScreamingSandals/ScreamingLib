package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.Wrapper;

@Data
@RequiredArgsConstructor
public class SenderWrapper implements Wrapper, CommandSenderWrapper {
    private final String name;
    private final Type type;

    public void sendMessage(String message) {
        PlayerMapper.sendMessage(this, message);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PlayerMapper.convertSenderWrapper(this, type);
    }

    @Override
    @NonNull
    public Audience audience() {
        return as(Audience.class);
    }

}