package org.screamingsandals.lib.sender;

import net.kyori.adventure.audience.ForwardingAudience;
import org.screamingsandals.lib.utils.Wrapper;

public interface CommandSenderWrapper extends Wrapper, ForwardingAudience.Single {

    Type getType();

    void sendMessage(String message);

    enum Type {
        PLAYER,
        CONSOLE
    }
}
