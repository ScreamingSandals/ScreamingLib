package org.screamingsandals.lib.proxiedplayer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.Wrapper;

@Data
@RequiredArgsConstructor
public class ProxiedSenderWrapper implements Wrapper, ForwardingAudience.Single {
    private final String name;
    private final Type type;

    public void sendMessage(String message) {
        ProxiedPlayerMapper.sendMessage(this, message);
    }

    @Override
    public <T> T as(Class<T> type) {
        return ProxiedPlayerMapper.convertSenderWrapper(this, type);
    }

    @Override
    public @NonNull Audience audience() {
        return as(Audience.class);
    }

    public enum Type {
        PLAYER,
        CONSOLE
    }

}